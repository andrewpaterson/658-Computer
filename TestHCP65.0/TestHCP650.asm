M16_I16	MACRO
	REP		#$30    ;16 bit registers
	LONGI 	ON
	LONGA	ON
	MACEND

M16	MACRO
	REP		#$20    ;16 bit memory
	LONGA	ON
	MACEND
	
I16	MACRO
	REP		#$10    ;16 bit index
	LONGI	ON
	MACEND
	
M8_I8	MACRO
	SEP		#$30    ;8 bit registers
	LONGI 	OFF
	LONGA	OFF
	MACEND

M8	MACRO
	SEP		#$20    ;8 bit memory
	LONGA	OFF
	MACEND
	
I8	MACRO
	SEP		#$10    ;8 bit index
	LONGI	OFF
	MACEND
	

INTERRUPT	MACRO   
	M16_I16
	PHB				;save DB
	PHD				;save DP
	PHA				;save A
	PHX				;save X
	PHY 			;save Y
	MACEND

STARTUP SECTION



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	
	ORG $4000
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	
	ORG $8000
RESET:
	CLC	     		;clear carry
	XCE	     		;clear emulation
	M16_I16
	LDA		#$6E00  ;get the stack address
	TCS	     		;and set the stack to it

	M8
	LDA 	#$00	;data bank 00
	PHA
	PLB	     		;set data bank register
	
	LDA		$6F02
	ORA		#%00000010
	STA		$6F02

	LDA		#$FE	;user program 1 (0xFF - user program ID)
	STA		$6F00
	
	LDA		#$42
	STA		$01FE00	;remap user progream 0xFF, bank 0x00 to real bank 0x42
	LDA		#$43
	STA		$01FE01	;remap user progream 0xFF, bank 0x01 to real bank 0x43

	LDA 	#$42	;data bank 0x42
	PHA
	PLB	     		;set data bank register
	
	LDX		#$FFFF	;user stack pointer
	LDA		#$00	;user bank (mapped)
	STA		$0000,X
	DEX
	LDA		#$20	;user program counter high
	STA		$0000,X
	DEX
	LDA		#$58	;user program counter low
	STA		$0000,X
	DEX
	PHP
	PLA				;user processor status
	STA		$0000,X
	DEX
	
	TXS				;set user stack
	
	RTI
	
IRQ:
	INTERRUPT
	BRA 	EXIT_INTERRUPT 

NMI:
	INTERRUPT
	BRA 	EXIT_INTERRUPT 

ABORT:
	INTERRUPT
	BRA 	EXIT_INTERRUPT 

COP:
	INTERRUPT
	BRA 	EXIT_INTERRUPT 

BRK:
	INTERRUPT
	BRA 	EXIT_INTERRUPT 

EMU:
	INTERRUPT
	BRA 	EXIT_INTERRUPT 

EXIT_INTERRUPT:
	M16_I16
	PLY             ;restore Y
    PLX             ;restore X
    PLA             ;restore A
    PLD             ;restore DP
    PLB             ;restore DB
	RTI
	
	
; This section defines the interrupt and reset vectors.    

	ORG	$FFE4

N_COP   DW    COP
N_BRK   DW    BRK
N_ABORT DW    ABORT
N_NMI   DW    NMI
N_RSRVD DW    0
N_IRQ   DW    IRQ

	ORG	$FFF4
E_COP   DW    EMU
E_RSRVD DW    0
E_ABORT DW    EMU
E_NMI   DW    EMU
E_RESET DW    RESET
E_IRQ   DW    EMU

	ENDS
	END
