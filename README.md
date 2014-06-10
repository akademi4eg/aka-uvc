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
* 0000	AND
* 1000	OR
* 0100	XOR
* 1100	NOT

bits
* 0010	SHL
* 1010	SHR
* 0110	ROL
* 1110	ROR

arithmetics
* 0001	ADD
* 1001	SUB
* 0101	INC
* 1101	DEC

util
* 0011	XCHG
* 1011	CMP
* 0111	reserved
* 1111	reserved