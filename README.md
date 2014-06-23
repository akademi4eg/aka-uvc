aka-uvc
=======

Useless Virtual Circuits

If everything ok, this evolve to emulator of a simple computer.

Implemented elements:
* basic logic gates (NOT and AND)
* other logic gates based on basic ones
* clocking device, multiplexer and flip-flops
* register and vectorized versions of logic operations
* bits manipulations (shifts and rotations)
* arithmetics (adder, two's complement etc.)
* ALU is under construction

ALU control codes:

logic
* 0000	AND (bitwise AND)
* 1000	OR (bitwise OR)
* 0100	XOR (bitwise XOR)
* 1100	NOT (bitwise NOT)

bits
* 0010	SHL (shift left)
* 1010	SHR (shift right)
* 0110	ROL (rotate left)
* 1110	ROR (rotate right)

arithmetics
* 0001	ADD (add to numbers)
* 1001	SUB (subtract two numbers)
* 0101	INC (increment)
* 1101	DEC (decrement)

util
* 0011	CMPNZ (check if is not zero)
* 1011	CMP
* 0111	reserved
* 1111	reserved