; Luis R. de la Vega López
; 802-09-1956
; Converts a decimal number (max 24 digits) to its equivalent hexadecimal number
;
; The decimal number is stored in the directives labeled MyDATAX (X=1,2,3,4,5,6)
; Example: If the number is 1802091956, then it should be placed like so:
; 1802091956 = 000000000000001802091956
; MyDATA1 0000h, MyDATA2 0000h, MyDATA3 0000h
; MyDATA4 0018h, MyDATA5 0209h, MyDATA6 1956h
;
; The result can be seen in registers R11(MSW), R12, R13, R14 and R15(LSW)
; Example: For decimal 1802091956 then the result will be 6B69BDB4h:
; R11= 0000h, R12= 0000h, R13= 0000h, R14= 6B69h, R15= BDB4h

#include	"msp430.h"
;-------------------------------------------------------------------------------
	ORG	0F800h	; Program start
;-------------------------------------------------------------------------------
Extract_Left_Nibble	MACRO	data,extractor
	LOCAL	Repeat
	mov	#0,extractor	; Initialize extractor
	mov	#4,R7	; Initialize counter
Repeat:	rla	data	; MSB to carry
	rlc	extractor	; carry to LSB
	dec	R7
	jnz	Repeat	; while counter > 0, Repeat
	ENDM

Mult_by_ten	MACRO	data
	LOCAL	Repeat, Mult
	mov	#3,R8	; Initialize counter
Repeat:	cmp	#2,R8	; Has the number been shifted only once?
	jnz	Mult	; jump if the number has not been shifted only once
	push	R11	; store 2*R11
	push	R12	; store 2*R12
	push	R13	; store 2*R13
	push	R14	; store 2*R14
	push	data	; store 2*data
Mult:	rla	data	; 2*data, 4*data, 8*data
	rlc	R14	; The following rlc instructions make sure ...
	rlc	R13	; ... that the number is treated as a whole and ...
	rlc	R12	; ... the whole number is multiplied
	rlc	R11
	dec	R8
	jnz	Repeat	; while counter > 0, Repeat
	add	@SP+,data	; 10*data and restore TOS
	addc	#0,R14	; Add the carry of the previous operation and ...
	add	@SP+,R14	; ... complete the mutiplication. Restore TOS
	addc	#0,R13	; Add the carry of the previous operation and ...
	add	@SP+,R13	; ... complete the mutiplication. Restore TOS
	addc	#0,R12	; Add the carry of the previous operation and ...
	add	@SP+,R12	; ... complete the mutiplication. Restore TOS
	addc	#0,R11	; Add the carry of the previous operation and ...
	add	@SP+,R11	; ... complete the mutiplication. Restore TOS
	ENDM
	
Main_Action	MACRO	myData	; Performs the steps to convert form Dec to Hex
	LOCAL	Repeat
	mov	myData,R4	; Stores the digits to be operated upon in R4
	mov	#4,R6	; Initialize counter
Repeat:	Mult_by_ten	R15	; P=P*A
	Extract_Left_Nibble	R4,R5	; Extract NX, X = counter-1
	add	R5,R15	; Update conversion or initialize it if R15 stores #0
	dec	R6
	jnz	Repeat	; while counter > 0, Repeat
	ENDM

MyDATA1	DW	0000h	; first 4 digits of users number (From left to right)
MyDATA2	DW	0000h	; digits 5-8 (From left to right)
MyDATA3	DW	0000h	; digits 9-12 (From left to right)
MyDATA4	DW	0018h	; digits 13-16 (From left to right)
MyDATA5	DW	0209h	; digits 17-20 (From left to right)
MyDATA6	DW	1956h	; digits 21-24 (From left to right)
RESET	mov	#280h,SP	; Initialize SP
StopWDT	mov	#WDTPW+WDTHOLD,&WDTCTL	; stop WDT
	mov.w	#0,R11	; Will store the MSW if result needs at least 12 bytes
	mov.w	#0,R12	; Will store the MSW if result needs at least 10 bytes
	mov.w	#0,R13	; Will store the MSW if result needs at least 8 bytes
	mov.w	#0,R14	; Will store the MSW if result needs at least 4 bytes
	mov.w	#0,R15	; Will store the MSW if result needs at least 2 bytes
	Main_Action	&MyDATA1	; Operates on the first 4 digits
	Main_Action	&MyDATA2	; Operates on digits 5-8
	Main_Action	&MyDATA3	; Operates on digits 9-12
	Main_Action	&MyDATA4	; Operates on digits 13-16
	Main_Action	&MyDATA5	; Operates on digits 17-20
	Main_Action	&MyDATA6	; Operates on digits 21-24
	jmp	$	; Breakpoint
;-------------------------------------------------------------------------------
 ;	Interrupt Vectors
;-------------------------------------------------------------------------------
	ORG	0xFFFE	; MSP430 Reset Vector
	DW	RESET	; address label RESET
	END