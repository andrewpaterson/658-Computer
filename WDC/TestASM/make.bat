call clean.bat
cls
@echo %time%

wdc816as.exe  -g -l -DUSING_816 -DLARGE -o Test65816.obj Test65816.asm || exit /b
wdc816as.exe  -g -l -DUSING_816 -DLARGE -o TestArithmetic.obj TestArithmetic.asm || exit /b
wdcln.exe -C200,00000 -g -t -sz -HB -obin\Test65816.bin Test65816.obj -lcl -lml || exit /b
wdcln.exe -C200,00000 -g -t -sz -HB -obin\TestArithmetic.bin TestArithmetic.obj -lcl -lml || exit /b
