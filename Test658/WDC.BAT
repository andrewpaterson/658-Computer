del /f Test658Kernal.bin Test658Kernal.lst  Test658Kernal.obj
del /f Test658User.bin Test658User.lst  Test658User.obj
WDC816AS.exe -L Test658Kernal.asm
WDCLN -HB Test658Kernal.obj
WDC816AS.exe -L Test658User.asm
WDCLN -HB Test658User.obj
dir *.BIN
