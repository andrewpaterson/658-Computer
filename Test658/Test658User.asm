	MACFIRST ON
COP	MACRO OP            ;co-processor
	IF OP>$FF
		EXIT "SIGNATURE MUST BE $00 - $FF"
	ENDIF
	DB $02, OP
	MACEND
	MACFIRST OFF


STARTUP SECTION

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


	ORG $02041c
	REP		#$10    ;16 bit index
	LONGI 	ON
	SEP		#$20    ;8 bit memory
	LONGA 	OFF
	
	LDA 	#$01	;get bank of data
	PHA
	PLB	     		;set data bank register

	LDX		#$0000

	REP		#$20    ;16 bit memory
	LONGA 	ON

	LDA		#$ABCD
	
LOOP:	
	STA		$1000,X
	INX
	INX
	
	CPX		#$08
	BNE		LOOP

FOREVER:	
	COP		$03
	BRA		FOREVER
