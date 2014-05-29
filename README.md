aka-uvc
=======

Useless Virtual Circuits

If everything ok, this will evolve to emulator of a simple computer.

Implemented elements:
* basic logic gates (NOT and AND)
* other logic gates based on basic ones
* clocking device, multiplexer and flip-flops
* register and vectorized versions of logic operations
* arithmetics (adder, two's complement etc.)
* ALU is under construction

ALU control codes:
logic
0000	AND
0001	OR
0010	XOR
0011	NOT
bits
0100	SHL
0101	SHR
0110	ROL
0111	ROR
arithmetics
1000	ADD
1001	SUB
1010	INC
1011	DEC
util
1100	XCHG
1101	CMP
1110	reserved
1111	reserved