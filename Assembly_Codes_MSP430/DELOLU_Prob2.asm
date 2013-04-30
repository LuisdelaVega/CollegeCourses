; Luis R. de la Vega López
; 802-09-1956
; Multiplication of two signed numbers from -32,768 to +32,767
;
; The numbers to be multiplied are stored in R7 and R12
;
; The result of the multiplication can be seen in registers R9(MSW) and R10(LSW)
; Example: If the multiplication is -32768*32767 = -1,073,709,056 = C0008000h:
; R9= C000h, R10= 8000h

#include	"msp430.h"
;-------------------------------------------------------------------------------
	ORG	0F800h	; Program Start
;-------------------------------------------------------------------------------
RESET	mov.w	#0280h,SP	; Initialize stackpointer
StopWDT	mov.w	#WDTPW+WDTHOLD,&WDTCTL	; Stop WDT
	mov.w	#0,R4	; Will be used to count how many multiplicands are neg
	mov.w	#0,R5	; Will be used to determine negative values
	mov.w	#0,R6	; Will update the result
	mov.w	#0,R9	; Will store the most significant word of the result
	mov.w	#0,R10	; Will store the least significant word of the result
	mov.w	#0,R11	; Will store the most significant word of N
	mov.w	#-32768,R7	; First value for the multiplication (M)
	mov.w	#32767,R12	; Second value for the multiplication (N)
	
Check1	bit.w	#1000000000000000b,R7	; Check if M stores a negative value
	jz	Check2	; If M is positive, jump to check the other operand
	sub.w	R7,R5	; 0-M -> R5
	mov.w	R5,R7	; 2's complement of M
	mov.w	#0,R5	; R5 will be used again and needs to be 0
	add.w	#1,R4	; Count the negative values
	
Check2	bit.w	#1000000000000000b,R12	; Check if N stores a negative value
	jz	MAIN	; If N is positive, jump to start the multiplication
	sub.w	R12,R5	; 0-N -> R5
	mov.w	R5,R12	; 2's complement of N
	mov.w	#0,R5	; R5 will be used again and needs to be 0
	add.w	#1,R4	; Count the negative values
	
MAIN	cmp.w	R7,R5	; Is M equal to 0?
	jn	Mult	; While M > 0, Keep iterating
	jmp	Check3	; Done iterating
	
Mult	bit.w	#1,R7	; Check if M is odd or even, if odd ZF=0
	jnz	ODDNUM	; If M is odd, jump
	
Shift	clrc	; Clears CF for integer division
	rrc	R7	; Integer division (M/2)
	rla	R12	; N*2
	addc	R11,R11	; Updates the MSW of N*2 (Add carry and multiply by 2)
	jmp	MAIN	; Again
	
ODDNUM	add	R12,R6	; Add N to R6 too update the result
	addc	R11,R9	; Used if a carry is generated, which means that ...
			; ... one register is not enough to hold the result
	jmp	Shift	; Done adding, now move to shifting
	
Check3	cmp	#1,R4	; Checks if the result must be positive or negative
	jnz	Finish	; Result must be positive
	sub	R6,R5	; 0-R6 -> R5
	mov.w	R5,R6	; 2's complement of R6
	mov.w	#0,R5	; R5 will be used again and needs to be 0
	sub	R9,R5	; 0-R9 -> R5
	mov.w	R5,R9	; 
	dec	R9	; decriment by 1 to get the 2's complement of R9
	mov.w	#0,R5	; For consistency we set the value of R5 to 0
	
Finish	mov.w	R6,R10	; Multiplication done. LSW of result is stored in R10
	jmp	$	; Breakpoint
;-------------------------------------------------------------------------------
 ;	Interrupt Vectors
;-------------------------------------------------------------------------------
	ORG	0FFFEh	; MSP430 Reset Vector
	DW	RESET	; address label RESET
	END
