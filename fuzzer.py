"""
Fuzzer for the Reliable 2048 CLI.

Structure follows the Fuzzing Book (Zeller et al.):
  https://www.fuzzingbook.org/

A Fuzzer generates inputs; a Runner executes the program under test and
classifies the outcome.  The two are kept separate so that different fuzzers
can be combined with the same runner, and vice-versa.
"""

import subprocess
import random
from typing import Tuple

# ---------------------------------------------------------------------------
# Outcome constants  (mirrors the Fuzzing Book convention)
# ---------------------------------------------------------------------------

PASS = "PASS"       # program exited normally, no errors reported
FAIL = "FAIL"       # program signalled a bug (non-zero exit, assertion error)
UNRESOLVED = "UNRESOLVED"  # e.g. timeout or unexpected error in the runner
MUTANTE_KILL = "MUTANTE_KILL"  # the mutante was killed by the test

# ---------------------------------------------------------------------------
# Valid input alphabet
# ---------------------------------------------------------------------------

KEYS = ['a', 's', 'w', 'd']   # left, down, up, right
QUIT = 'q'                     # graceful exit

# ---------------------------------------------------------------------------
# Runner
# ---------------------------------------------------------------------------

class Runner:
    """Runs the program under test and returns (output, outcome)."""

    def run(self, inp: str) -> Tuple[subprocess.CompletedProcess, str]:
        raise NotImplementedError


class CLIRunner(Runner):
    """
    Runs the 2048 CLI as a subprocess, feeding `inp` to its stdin.

    Returns a (CompletedProcess, outcome) pair where outcome is one of
    PASS, FAIL, or UNRESOLVED.
    """

    COMMAND = ['java','-ea', '-cp','./target/classes', 'ar.edu.unrc.game2048.MainCLI']
    TIMEOUT = 10  # seconds

    def run(self, inp: str) -> Tuple[subprocess.CompletedProcess, str]:
        try:
            result = subprocess.run(
                self.COMMAND,
                input=inp,
                capture_output=True,
                text=True,
                timeout=self.TIMEOUT
            )
        except subprocess.TimeoutExpired:
            return None, UNRESOLVED

        if result.returncode != 0 or result.stderr:
            return result, FAIL

        return result, PASS

class GrammarCLIRunner(Runner):

    COMMAND = ['java', '-ea', '-cp', './target/classes', 'ar.edu.unrc.game2048.MainCLI', 's']
    TIMEOUT = 10

    def run(self, inp: str) -> Tuple[subprocess.CompletedProcess, str]:
        try:
            result = subprocess.run(
                self.COMMAND,
                input=inp,
                capture_output=True,
                text=True,
                timeout=self.TIMEOUT
            )
        except subprocess.TimeoutExpired:
            return None, UNRESOLVED

        if result.returncode != 0 or result.stderr:
            return result, FAIL

        return result, PASS

class MutationCLIRunner(Runner):

    BASE = ['java', '-ea', '-cp', './target/classes', 'ar.edu.unrc.game2048.MainCLI']
    TIMEOUT = 10

    def __init__(self, board_mode: bool = True):
        if board_mode:
            self.command = self.BASE + ['s']
        else:
            self.command = self.BASE

    def run(self, inp: str) -> Tuple[subprocess.CompletedProcess, str]:
        try:
            result = subprocess.run(
                self.command,
                input=inp,
                capture_output=True,
                text=True,
                timeout=self.TIMEOUT
            )
        except subprocess.TimeoutExpired:
            return None, UNRESOLVED

        if "IllegalArgumentException" in result.stderr:
            return result, MUTANTE_KILL

        if result.returncode == 0 and not result.stderr:
            return result, PASS

        return result, FAIL

# ---------------------------------------------------------------------------
# Fuzzer base class
# ---------------------------------------------------------------------------

class Fuzzer:
    """Base class: subclasses implement fuzz() to produce one input string."""

    def fuzz(self) -> str:
        """Return a single fuzzed input string."""
        raise NotImplementedError

    def run(self, runner: Runner) -> Tuple[subprocess.CompletedProcess, str]:
        """Fuzz once and run the result through *runner*."""
        return runner.run(self.fuzz())

    def runs(self, runner: Runner, trials: int = 10):
        """Fuzz *trials* times and return a list of (result, outcome) pairs."""
        return [self.run(runner) for _ in range(trials)]


# ---------------------------------------------------------------------------
# RandomFuzzer
# ---------------------------------------------------------------------------

class RandomFuzzer(Fuzzer):
    """
    Generates a random sequence of game moves followed by a quit command.

    Parameters
    ----------
    min_length : int
        Minimum number of move keys in the sequence.
    max_length : int
        Maximum number of move keys in the sequence.
    """

    def __init__(self, min_length: int = 10, max_length: int = 50):
        self.min_length = min_length
        self.max_length = max_length

    def fuzz(self) -> str:
        length = random.randint(self.min_length, self.max_length)
        moves = []

        for _ in range(length):
            moves.append(random.choice(KEYS))

        return "\n".join(moves) + "\n" + QUIT + "\n"
        """
        TODO: Implement this method.

        Return a string representing a sequence of moves for the 2048 CLI.
        Each key must be on its own line (followed by '\\n').
        The sequence must end with the quit key ('q') on its own line.

        Use `self.min_length` and `self.max_length` to decide how many moves
        to generate, and pick keys randomly from the KEYS list.

        Example output for a sequence of 3 moves:
            'w\\na\\nd\\nq\\n'
        
        raise NotImplementedError
        """

# ---------------------------------------------------------------------------
# GrammarBasedFuzzer
# ---------------------------------------------------------------------------

class GrammarBasedFuzzer(Fuzzer):

    GRAMMAR = { "<cell>": [
            "0", "0", "0", "0",
            "2", "2", "2", "2",
            "4", "4", "4", "4",
            "8", "8", "16", "16",
            "32", "64", "128", "256",
            "512", "1024", "2048"
        ]
    }

    def __init__(self, min_moves: int = 10, max_moves: int = 50, min_size: int = 4, max_size: int = 19):
        self.min_moves = min_moves
        self.max_moves = max_moves
        self.min_size = min_size
        self.max_size = max_size

    def fuzz(self) -> str:
        aux = random.randint(self.min_size, self.max_size)
        size = aux * aux
        board = "["
        for i in range(size):
            if i > 0:
                board += ","
            board += random.choice(self.GRAMMAR["<cell>"])
        board += "]"

        length_moves = random.randint(self.min_moves, self.max_moves)
        moves = []

        for _ in range(length_moves):
            moves.append(random.choice(KEYS))

        return board + "\n" + "\n".join(moves) + "\n" + QUIT + "\n"

# ---------------------------------------------------------------------------
# Mutator
# ---------------------------------------------------------------------------

class Mutator(Fuzzer):

    MUTATION = {
        "operations": ["add", "modify", "delete"],
        "chars": [ 
            "a", "b", "c", "d", "e", "d",
            "(", ")", "{", "}", "[", "]",
            "0", "1", "2", "3", "4", "5", 
            "6", "7", "8", "9", ",", "-"
        ]
    }

    def __init__(self, base: Fuzzer, min_mutations: int = 1, max_mutations: int = 3):
            self.base = base
            self.min_mutations = min_mutations
            self.max_mutations = max_mutations

    def fuzz(self) -> str:
        board, _, moves = self.base.fuzz().partition("\n")
        self.original = board  

        for _ in range(random.randint(self.min_mutations, self.max_mutations)):
            board = self.mutate(board)

        return board + "\n" + moves

    def mutate(self, inp: str) -> str:
        char = random.choice(self.MUTATION["chars"])
        if not inp:
            return char

        operation = random.choice(self.MUTATION["operations"])
        aux = list(inp)

        if operation == "add":
            aux.insert(random.randint(0, len(inp)), char)
        elif operation == "modify":
            aux[random.randint(1, len(aux)) - 1] = char
        else:
            del aux[random.randint(1, len(aux)) - 1]

        return "".join(aux)

# ---------------------------------------------------------------------------
# Main: run the fuzzer and report results
# ---------------------------------------------------------------------------

def main():
    runner = MutationCLIRunner(board_mode=True)
    fuzzer = Mutator(GrammarBasedFuzzer(min_moves=1, max_moves=15, min_size=1, max_size=10), min_mutations=1, max_mutations=5)

    trials = 50
    outcomes = {PASS: 0, FAIL: 0, UNRESOLVED: 0, MUTANTE_KILL: 0}

    print(f"Running {trials} fuzzing trials...\n")

    for i in range(trials):
        inp = fuzzer.fuzz()
        result, outcome = runner.run(inp)

        outcomes[outcome] += 1

        print(f"--- Trial {i + 1:>3} | {outcome} ---")
        print(f"Input : {inp.replace(chr(10), ' ').strip()}")
        print(f"Original: {fuzzer.original}")

        if result is not None:
            print(f"Exit  : {result.returncode}")
            if result.stdout:
                print(f"Output: {result.stdout.strip()}")
            if result.stderr:
                print(f"Stderr: {result.stderr.strip()}")

        print()

    print("=" * 50)
    print("Summary:")
    for outcome, count in outcomes.items():
        print(f"  {outcome:<12}: {count}/{trials}")


if __name__ == "__main__":
    main()
