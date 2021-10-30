;

STARTUP SECTION

	ORG	$0000
	DB '['
PROGRESS:	
	DB 'xxxx'
PROGRESS_NMI:		
	DB 'xxxx'
PROGRESS_ABORT:
	DB 'xxxxxx'
PROGRESS_BRK:
	DB 'xxxx'
PROGRESS_COP:
	DB 'xxx'
ENDPROGRESS:	
	DB ']'
	
	ORG $E12B
RESET:
	CLC	     		;clear carry
	XCE	     		;clear emulation
	REP		#$30    ;16 bit registers
	LONGI 	ON
	LONGA	ON
	LDA		#$01FF  ;get the stack address
	TCS	     		;and set the stack to it


	SEP		#$20	;8 bit accumulator
	LONGA 	OFF
	LDA 	#$00	;get bank of data
	PHA
	PLB	     		;set data bank register
	REP		#$20    ;back to 16 bit mode
	LONGA	ON

	JML  	START	;long jump in case not bank 0

IRQ:
	PHB				;save DB
	PHD				;save DP
	REP		#$30	;16 bit registers
	LONGI 	ON
	LONGA	ON
	PHA				;save A
	PHX				;save X
	PHY 			;save Y
	
	LDX 	#IRQ_STRING
	LDY 	#PROGRESS
	JSR 	WRITE_STRING
	
	BRA 	EXIT_INTERRUPT 

NMI:
	PHB				;save DB
	PHD				;save DP
	REP		#$30	;16 bit registers
	LONGI 	ON
	LONGA	ON
	PHA				;save A
	PHX				;save X
	PHY 			;save Y

	LDX 	#NMI_STRING
	LDY 	#PROGRESS_NMI
	JSR 	WRITE_STRING
	
	BRA 	EXIT_INTERRUPT 

ABORT:
	PHB				;save DB
	PHD				;save DP
	REP		#$30	;16 bit registers
	LONGI 	ON
	LONGA	ON
	PHA				;save A
	PHX				;save X
	PHY 			;save Y
	
	LDX 	#ABORT_STRING
	LDY 	#PROGRESS_ABORT
	JSR 	WRITE_STRING

	BRA 	EXIT_INTERRUPT 

COP:
	PHB				;save DB
	PHD				;save DP
	REP		#$30	;16 bit registers
	LONGI 	ON
	LONGA	ON
	PHA				;save A
	PHX				;save X
	PHY 			;save Y

	LDX 	#COP_STRING
	LDY 	#PROGRESS_COP
	JSR 	WRITE_STRING
	
	BRA 	EXIT_INTERRUPT 

BRK:
	PHB				;save DB
	PHD				;save DP
	REP		#$30	;16 bit registers
	LONGI 	ON
	LONGA	ON
	PHA				;save A
	PHX				;save X
	PHY 			;save Y

	LDX 	#BRK_STRING
	LDY 	#PROGRESS_BRK
	JSR 	WRITE_STRING
	
	BRA 	EXIT_INTERRUPT 

EMU:
	PHB				;save DB
	PHD				;save DP
	REP		#$30	;16 bit registers
	LONGI 	ON
	LONGA	ON
	PHA				;save A
	PHX				;save X
	PHY 			;save Y
	
	LDA 	#"E"
	JSR 	WRITE_PROGRESS

	BRA 	EXIT_INTERRUPT 

EXIT_INTERRUPT:
	REP	#$30		;16 bit registers
	LONGI 	ON
	LONGA	ON
	PLY             ;restore Y
    PLX             ;restore X
    PLA             ;restore A
    PLD             ;restore DP
    PLB             ;restore DB
	RTI
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
	
	ORG $8000
START:
	CLI
	LDA 	#"."
	JSR 	WRITE_PROGRESS
	BRK		12
	COP		34
	
LOOP:
	JMP 	LOOP

WRITE_PROGRESS:
	SEP		#$20	;8 bit accumulator
	LONGA 	OFF

	LDY 	#PROGRESS
LOOP_DOT:	
	STA 	0,Y
	INY
	CPY 	#ENDPROGRESS
	BNE 	LOOP_DOT
	RTS
	
WRITE_STRING:	
	SEP		#$20	;8 bit accumulator
	LONGA 	OFF
WRITE_STRING_LOOP:	
	LDA 	0,X
	BEQ 	DONE_STRING
	STA 	0,Y
	INY
	INX
	BRA 	WRITE_STRING_LOOP
DONE_STRING:
	REP		#$20    ;back to 16 bit mode
	LONGA 	ON
	RTS

IRQ_STRING:	
	DB 'IRQ'
	DB #0
NMI_STRING:	
	DB 'NMI'
	DB #0
ABORT_STRING:	
	DB 'ABORT'
	DB #0
BRK_STRING:	
	DB 'BRK'
	DB #0
COP_STRING:	
	DB 'COP'
	DB #0
EMULATION_STRING:	
	DB ' - Emulation -  '
	DB #0

	
; This section defines the interrupt and reset vectors.    

	ORG	$FFE4

N_COP   DW    COP
N_BRK   DW    BRK
N_ABORT DW    ABORT
N_NMI   DW    NMI
N_RSRVD DW    0
N_IRQ   DW    IRQ
	DS    4
E_COP   DW    EMU
E_RSRVD DW    0
E_ABORT DW    EMU
E_NMI   DW    EMU
E_RESET DW    RESET
E_IRQ   DW    EMU

	ENDS
	END
