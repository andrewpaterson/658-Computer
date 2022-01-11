del /f TestHCP650.bin TestHCP650.lst TestHCP650.obj
cls
WDC816AS.exe -L TestHCP650.asm
WDCLN -HB TestHCP650.obj

