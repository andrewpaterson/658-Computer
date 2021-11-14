del /f Test658.bin Test658.lst  Test658.obj
WDC816AS.exe -L Test658.asm
WDCLN -HB Test658.obj
dir *.BIN