;

STARTUP SECTION

	ORG	$0000
	DB '['
PROGRESS:	
	DB 'xxxxxxxxxxxxxxxx'
ENDPROGRESS:	
	DB ']'
	
	ORG $F12B
RESET:
	CLC	     		;clear carry
	XCE	     		;clear emulation
	REP		#$30    ;16 bit registers
	LONGI 	ON
	LONGA	ON
	LDA		#$0100  ;get the stack address
	TCS	     		;and set the stack to it


	SEP		#$20	;8 bit accumulator
	LONGA 	OFF
	LDA 	#$00	;get bank of data
	PHA
	PLB	     		;set data bank register
	REP		#$20    ;back to 16 bit mode
	LONGA	ON

	JMP   START		;long jump in case not bank 0

IRQ:
	PHB				;save DB
	PHD				;save DP
	REP	#$30		;16 bit registers
	PHA				;save A
	PHX				;save X
	PHY 			;save Y
	 
	REP	#$30		;16 bit registers
	PLY             ;restore Y
    PLX             ;restore X
    PLA             ;restore A
    PLD             ;restore DP
    PLB             ;restore DB
    RTI				;return

	ORG $8000
START
	CLI
	SEP		#$20	;8 bit accumulator
	LONGA 	OFF

	LDA #$2e
	LDY #PROGRESS
LOOP:	
	STA 0,Y
	INY
	CPY #ENDPROGRESS
	BNE LOOP
	STP
	
; This section defines the interrupt and reset vectors.    

	ORG	$FFE4

N_COP   DW    0
N_BRK   DW    0
N_ABORT DW    0
N_NMI   DW    0
N_RSRVD DW    0
N_IRQ   DW    0
	DS    4
E_COP   DW    0
E_RSRVD DW    0
E_ABORT DW    0
E_NMI   DW    0
E_RESET DW    RESET
E_IRQ   DW    IRQ

	ENDS
	END
