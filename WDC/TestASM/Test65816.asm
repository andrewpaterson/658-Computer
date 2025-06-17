STARTUP SECTION


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;	
	
	ORG $0200
RESET:
	CLC	     		;clear carry
	XCE	     		;clear emulation
	REP		#$30    ;16 bit registers
	LONGI 	ON
	LONGA	ON
	LDA		#$01FF  ;get the stack address
	TCS	     		;and set the stack to it

	SEP		#$20	;8 bit memory
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

	SEP		#$20	;8 bit memory
	LONGA 	OFF
	LDA 	#$00	;get bank of data
	PHA
	PLB	     		;set data bank register

	LDX		$6F0C	;instruction counter byte 0 and 1
	LDY		$6F0E	;instruction counter byte 2 and 3

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
	
START:
	LDA		$FFFC
	CMP		#$0200
	
	BEQ		ABSOLUTE_READ_OKAY
	BRK
ABSOLUTE_READ_OKAY

	SEP		#$20	;8 bit memory
	LONGA	OFF
	
	LDA		#$A4
	STA		$7012	;echo device (device 1, address 2)
	LDA		#$CC
	LDA		$7012	;echo device (device 1, address 2)
	INC 	A
	STA		$7012	;echo device (device 1, address 2)
	LDA		#$CC
	LDA		$7012	;echo device (device 1, address 2)
	CMP		#$A5
	BEQ		READ_DEVICE_OKAY
	BRK
READ_DEVICE_OKAY

	LDX		#$CDEF
	TXY

	LDX		$6F0C	;instruction counter byte 0 and 1
	LDY		$6F0E	;instruction counter byte 2 and 3

	LDA		#$FF	;user program 0 (0xFF - user program ID)
	STA		$6F00
	
	LDA		#$42
	STA		$01FF00	;remap user progream 0xFF, bank 0x00 to real bank 0x42
	LDA		#$43
	STA		$01FF01	;remap user progream 0xFF, bank 0x01 to real bank 0x43
	
	LDA		#$00
	PHA				;push progream counter bank
	LDA		#$04
	PHA				;push progream counter high-byte
	LDA		#$1C
	PHA				;push progream counter low-byte
	PHP				;push processor status
	
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
