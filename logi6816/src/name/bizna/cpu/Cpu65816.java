package name.bizna.cpu;

import name.bizna.cpu.addressingmode.FetchOpCodeCycles;
import name.bizna.cpu.addressingmode.InstructionCycles;
import name.bizna.cpu.addressingmode.StackHardwareInterruptCycles;
import name.bizna.cpu.addressingmode.StackResetCycles;
import name.bizna.cpu.interrupt.AbortVector;
import name.bizna.cpu.interrupt.IRQVector;
import name.bizna.cpu.interrupt.NMIVector;
import name.bizna.cpu.interrupt.ResetVector;
import name.bizna.cpu.opcode.*;
import name.bizna.util.EmulatorException;
import name.bizna.util.IntUtil;

import static name.bizna.util.IntUtil.*;
import static name.bizna.util.StringUtil.to16BitHex;
import static name.bizna.util.StringUtil.to8BitHex;

public class Cpu65816
{
  // Status register
  protected CpuStatus processorStatus;
  protected Pins65816 pins;
  protected boolean stopped;

  protected int accumulator;
  protected int xIndex;
  protected int yIndex;
  protected int dataBank;
  protected int directPage;
  protected Address programAddress;
  protected int stackPointer;

  protected boolean previousClock;
  protected int cycle;
  protected OpCode opCode;

  // Real OpCode Table.
  protected static OpCode[] opCodeTable;
  protected static OpCode resetOpcode;
  protected static OpCode abortOpcode;
  protected static OpCode irqOpcode;
  protected static OpCode nmiOpcode;
  protected static OpCode fetchNextOpcode;

  //These are not the values on the pins, they are internal data.
  protected Address internalAddress;
  protected int internalData;
  protected int internalDirectOffset;
  protected Address internalProgramCounter;
  private boolean read;

  public Cpu65816(Pins65816 pins)
  {
    this.pins = pins;
    this.pins.setCpu(this);

    programAddress = new Address(0x00, 0x0000);
    stackPointer = 0x01FF;
    opCodeTable = OpCodeTable.createTable();
    resetOpcode = new OpCode_RES(new StackResetCycles(new ResetVector(), Cpu65816::RES));
    irqOpcode = new OpCode_IRQ(new StackHardwareInterruptCycles(new IRQVector(), Cpu65816::IRQ));
    nmiOpcode = new OpCode_NMI(new StackHardwareInterruptCycles(new NMIVector(), Cpu65816::NMI));
    abortOpcode = new OpCode_ABORT(new StackHardwareInterruptCycles(new AbortVector(), Cpu65816::ABORT));
    fetchNextOpcode = new OpCode_NEXT(new FetchOpCodeCycles());
    accumulator = 0;
    xIndex = 0;
    yIndex = 0;
    dataBank = 0;
    directPage = 0;
    processorStatus = new CpuStatus();
    stopped = false;
    previousClock = true;
    cycle = 1;
    opCode = resetOpcode;
    internalData = 0;
    internalDirectOffset = 0;
    internalProgramCounter = new Address();
    internalAddress = new Address();
  }

  public void setX(int xIndex)
  {
    if (isIndex16Bit())
    {
      assert16Bit(xIndex, "X Index register");
    }
    else
    {
      assert8Bit(xIndex, "X Index register");
    }
    this.xIndex = xIndex;
    setSignAndZeroFromIndex(xIndex);
  }

  public void setY(int yIndex)
  {
    if (isIndex16Bit())
    {
      assert16Bit(yIndex, "Y Index register");
    }
    else
    {
      assert8Bit(yIndex, "Y Index register");
    }
    this.yIndex = yIndex;
    setSignAndZeroFromIndex(yIndex);
  }

  public void setA(int accumulator)
  {
    if (isMemory16Bit())
    {
      assert16Bit(accumulator, "Accumulator");
      this.accumulator = accumulator;
    }
    else
    {
      assert8Bit(accumulator, "Accumulator");
      this.accumulator = setLowByte(this.accumulator, accumulator);
    }
    setSignAndZeroFromMemory(accumulator);
  }

  public void setC(int accumulator)
  {
    assert16Bit(accumulator, "Accumulator");
    this.accumulator = accumulator;
    processorStatus.setSignAndZeroFlagFrom16BitValue(accumulator);
  }

  public void setData(int data, boolean updateFlags)
  {
    if (isMemory16Bit())
    {
      assert16Bit(data, "Data");
      internalData = data;
    }
    else
    {
      assert8Bit(data, "Data");
      internalData = setLowByte(internalData, data);
    }
    if (updateFlags)
    {
      setSignAndZeroFromMemory(data);
    }
  }

  public void setIndexData(int data, boolean updateFlags)
  {
    if (isIndex16Bit())
    {
      assert16Bit(data, "Data");
      internalData = data;
    }
    else
    {
      assert8Bit(data, "Data");
      internalData = setLowByte(internalData, data);
    }
    if (updateFlags)
    {
      setSignAndZeroFromIndex(data);
    }
  }

  private void setSignAndZeroFromMemory(int value)
  {
    if (isMemory16Bit())
    {
      processorStatus.setSignAndZeroFlagFrom16BitValue(value);
    }
    else
    {
      processorStatus.setSignAndZeroFlagFrom8BitValue(value);
    }
  }

  private void setSignAndZeroFromIndex(int value)
  {
    if (isIndex16Bit())
    {
      processorStatus.setSignAndZeroFlagFrom16BitValue(value);
    }
    else
    {
      processorStatus.setSignAndZeroFlagFrom8BitValue(value);
    }
  }

  public void setData(int data)
  {
    setData(data, true);
  }

  public int getA()
  {
    if (isMemory16Bit())
    {
      return accumulator;
    }
    else
    {
      return toByte(accumulator);
    }
  }

  public int getC()
  {
    return accumulator;
  }

  public int getX()
  {
    if (isIndex16Bit())
    {
      return xIndex;
    }
    else
    {
      return toByte(xIndex);
    }
  }

  public int getY()
  {
    if (isIndex16Bit())
    {
      return yIndex;
    }
    else
    {
      return toByte(yIndex);
    }
  }

  public int getDataBank()
  {
    return dataBank;
  }

  public void setDataBank(int dataBank)
  {
    assert8Bit(dataBank, "Data Bank");
    this.dataBank = dataBank;
  }

  public void setDirectPage(int directPage)
  {
    assert16Bit(directPage, "Direct Page");
    this.directPage = directPage;
  }

  public Address getProgramCounter()
  {
    return programAddress;
  }

  public CpuStatus getCpuStatus()
  {
    return processorStatus;
  }

  public void tick()
  {
    boolean clock = pins.getPhi2();
    boolean clockFallingEdge = !clock && previousClock;
    boolean clockRisingEdge = clock && !previousClock;
    previousClock = clock;

    if (!clockFallingEdge && !clockRisingEdge)
    {
      return;
    }
    if (stopped)
    {
      return;
    }

    InstructionCycles cycles = opCode.getCycles();

    while (!cycles.mustExecute(this))
    {
      nextCycle();
    }

    if (clockFallingEdge)
    {
      pins.setEmulation(processorStatus.isEmulation());
      pins.setMX(isMemory8Bit());

      cycles.executeOnFallingEdge(this);
    }
    if (clockRisingEdge)
    {
      pins.setMX(isIndex8Bit());

      cycles.executeOnRisingEdge(this);
      nextCycle();
    }
  }

  public boolean isStopped()
  {
    return stopped;
  }

  public void setRead(boolean read)
  {
    this.read = read;
  }

  public boolean isRead()
  {
    return read;
  }

  public boolean isMemory8Bit()
  {
    if (processorStatus.isEmulation())
    {
      return true;
    }
    else
    {
      return processorStatus.isAccumulator8Bit();
    }
  }

  public boolean isMemory16Bit()
  {
    return !isMemory8Bit();
  }

  public boolean isIndex8Bit()
  {
    if (processorStatus.isEmulation())
    {
      return true;
    }
    else
    {
      return processorStatus.isIndex8Bit();
    }
  }

  public boolean isIndex16Bit()
  {
    return !isIndex8Bit();
  }

  public boolean isCarrySet()
  {
    return processorStatus.isCarry();
  }

  protected int getCarry()
  {
    return isCarrySet() ? 1 : 0;
  }

  private boolean isSignSet()
  {
    return processorStatus.isNegative();
  }

  private boolean isOverflowSet()
  {
    return processorStatus.isOverflowFlag();
  }

  public void setProgramAddress(Address address)
  {
    programAddress = address;
  }

  public void setProgramAddressBank(int bank)
  {
    assert8Bit(bank, "Program Address Bank");
    programAddress.bank = bank;
  }

  public void setAddressLow(int addressLow)
  {
    assert8Bit(addressLow, "Address Low");
    this.internalAddress.setOffsetLow(addressLow);
  }

  public void setAddressHigh(int addressHigh)
  {
    assert8Bit(addressHigh, "Address High");
    this.internalAddress.setOffsetHigh(addressHigh);
  }

  public void setAddressBank(int addressBank)
  {
    assert8Bit(addressBank, "Address Bank");
    this.internalAddress.setBank(addressBank);
  }

  public int getDataLow()
  {
    return getLowByte(internalData);
  }

  public int getDataHigh()
  {
    return getHighByte(internalData);
  }

  public int getData()
  {
    if (isMemory16Bit())
    {
      return internalData;
    }
    else
    {
      return toByte(internalData);
    }
  }

  public int getIndexData()
  {
    if (isIndex16Bit())
    {
      return internalData;
    }
    else
    {
      return toByte(internalData);
    }
  }

  public int getData16Bit()
  {
    return internalData;
  }

  public int getDirectPage()
  {
    return directPage;
  }

  public int getDirectOffset()
  {
    return internalDirectOffset;
  }

  public int getStackPointer()
  {
    if (!processorStatus.isEmulation())
    {
      return stackPointer;
    }
    else
    {
      return getLowByte(stackPointer) | 0x100;
    }
  }

  public void doneInstruction()
  {
    cycle = -1;
    opCode = fetchNextOpcode;
  }

  public void nextCycle()
  {
    cycle++;
  }

  public int getPinData()
  {
    if (pins.getPhi2())
    {
      return pins.getData();
    }
    else
    {
      throw new EmulatorException("Cannot get Data from pins when clock is low.");
    }
  }

  public int getCycle()
  {
    return this.cycle;
  }

  public void setOpCode(int data)
  {
    if ((data >= 0) && (data <= 255))
    {
      opCode = opCodeTable[data];
    }
    else
    {
      throw new EmulatorException("Invalid Op-code");
    }
  }

  public Address getAddress()
  {
    return internalAddress;
  }

  public OpCode getOpCode()
  {
    return opCode;
  }

  public Address getNewProgramCounter()
  {
    return internalProgramCounter;
  }

  public void incrementProgramAddress()
  {
    this.programAddress.offset(1, true);
  }

  public void decrementProgramCounter()
  {
    this.programAddress.offset(-1, true);
  }

  public void incrementStackPointer()
  {
    this.stackPointer++;
    stackPointer = toShort(stackPointer);
  }

  public void decrementStackPointer()
  {
    this.stackPointer--;
    stackPointer = toShort(stackPointer);
  }

  public void setDirectOffset(int data)
  {
    assert8Bit(data, "Direct Offset");
    internalDirectOffset = data;
  }

  public void setDataLow(int data)
  {
    assert8Bit(data, "Data Low");
    internalData = setLowByte(internalData, data);
  }

  public void setDataHigh(int data)
  {
    assert8Bit(data, "Data High");
    internalData = setHighByte(internalData, data);
  }

  public void setStackPointer(int data)
  {
    assert16Bit(data, "Stack Pointer");
    stackPointer = data;
  }

  public void setNewProgramCounterLow(int data)
  {
    assert8Bit(data, "Program Counter Low");
    internalProgramCounter.setOffsetLow(data);

  }

  public void setNewProgramCounterHigh(int data)
  {
    assert8Bit(data, "Program Counter High");
    internalProgramCounter.setOffsetHigh(data);
  }

  public void setNewProgramCounterBank(int data)
  {
    assert8Bit(data, "Program Counter Bank");
    internalProgramCounter.setBank(data);
  }

  public Pins65816 getPins()
  {
    return pins;
  }

  public void setEmulationMode(boolean emulation)
  {
    processorStatus.setEmulationFlag(emulation);
    if (emulation)
    {
      xIndex = toByte(xIndex);
      yIndex = toByte(yIndex);
      processorStatus.setAccumulatorWidthFlag(true);
      processorStatus.setIndexWidthFlag(true);
      stackPointer = toByte(stackPointer) | 0x100;
    }
  }

  public static boolean is8bitValueNegative(int value)
  {
    return (value & 0x80) != 0;
  }

  public static boolean is16bitValueNegative(int value)
  {
    return (value & 0x8000) != 0;
  }

  public static boolean is8bitValueZero(int value)
  {
    return (toByte(value) == 0);
  }

  public static boolean is16bitValueZero(int value)
  {
    return (toShort(value) == 0);
  }

  public boolean isEmulationMode()
  {
    return processorStatus.isEmulation();
  }

  private void processorStatusChanged()
  {
    if (isIndex8Bit())
    {
      xIndex = toByte(xIndex);
      yIndex = toByte(yIndex);
    }
    if (isEmulationMode())
    {
      processorStatus.setIndexWidthFlag(true);
      processorStatus.setAccumulatorWidthFlag(true);
    }
  }

  public void setCarryFlag(boolean carry)
  {
    processorStatus.setCarryFlag(carry);
  }

  private int setBit(int value, boolean bitValue, int bitNumber)
  {
    if (bitValue)
    {
      value = setBit(value, bitNumber);
    }
    else
    {
      value = clearBit(value, bitNumber);
    }
    return value;
  }

  public int clearBit(int value, int bitNumber)
  {
    return trimMemory(value & ~(1 << bitNumber));
  }

  public int setBit(int value, int bitNumber)
  {
    return trimMemory(value | (1 << bitNumber));
  }

  public void incrementA()
  {
    int a = trimMemory(getA() + 1);
    setA(a);
    setSignAndZeroFromMemory(a);
  }

  public void incrementX()
  {
    int x = trimIndex(getX() + 1);
    setX(x);
    setSignAndZeroFromIndex(x);
  }

  public void incrementY()
  {
    int y = trimIndex(getY() + 1);
    setY(y);
    setSignAndZeroFromIndex(y);
  }

  public void decrementA()
  {
    int a = trimMemory(getA() - 1);
    setA(a);
    setSignAndZeroFromMemory(a);
  }

  public void decrementY()
  {
    int y = trimIndex(getY() - 1);
    setY(y);
    setSignAndZeroFromIndex(y);
  }

  public void decrementX()
  {
    int x = trimIndex(getX() - 1);
    setX(x);
    setSignAndZeroFromIndex(x);
  }

  int rotateLeft(int value)
  {
    boolean carryWillBeSet = isMemoryNegative(value);
    value = trimMemory(value << 1);
    value = setBit(value, isCarrySet(), 0);
    setCarryFlag(carryWillBeSet);
    return value;
  }

  int rotateRight(int value)
  {
    boolean carryWillBeSet = (value & 1) != 0;
    value = trimMemory(value >> 1);
    value = setBit(value, isCarrySet(), isMemory16Bit() ? 15 : 7);
    setCarryFlag(carryWillBeSet);
    return value;
  }

  int shiftRight(int value)
  {
    setCarryFlag((value & 1) != 0);
    return trimMemory(value >> 1);
  }

  public BCDResult bcdAdd8Bit(int bcdFirst, int bcdSecond, boolean carry)
  {
    int shift = 0;
    int result = 0;

    while (shift < 8)
    {
      int digitOfFirst = (bcdFirst & 0xF);
      int digitOfSecond = (bcdSecond & 0xF);
      int sumOfDigits = toByte(digitOfFirst + digitOfSecond + (carry ? 1 : 0));
      carry = sumOfDigits > 9;
      if (carry)
      {
        sumOfDigits += 6;
      }
      sumOfDigits &= 0xF;
      result |= sumOfDigits << shift;

      shift += 4;
      bcdFirst >>= shift;
      bcdSecond >>= shift;
    }

    return new BCDResult(result, carry);
  }

  public BCDResult bcdAdd16Bit(int bcdFirst, int bcdSecond, boolean carry)
  {
    int result = 0;
    int shift = 0;
    while (shift < 16)
    {
      int digitOfFirst = (bcdFirst & 0xFF);
      int digitOfSecond = (bcdSecond & 0xFF);
      BCDResult bcd8BitResult = bcdAdd8Bit(digitOfFirst, digitOfSecond, carry);
      carry = bcd8BitResult.carry;
      int partialResult = bcd8BitResult.value;
      result = toShort(result | (partialResult << shift));
      shift += 8;
      bcdFirst = toShort(bcdFirst >> shift);
      bcdSecond = toShort(bcdSecond >> shift);
    }
    return new BCDResult(result, carry);
  }

  public BCDResult bcdSubtract8Bit(int bcdFirst, int bcdSecond, boolean borrow)
  {
    int shift = 0;
    int result = 0;

    while (shift < 8)
    {
      int digitOfFirst = (bcdFirst & 0xF);
      int digitOfSecond = (bcdSecond & 0xF);
      int diffOfDigits = toByte(digitOfFirst - digitOfSecond - (borrow ? 1 : 0));
      borrow = diffOfDigits > 9;
      if (borrow)
      {
        diffOfDigits -= 6;
      }
      diffOfDigits &= 0xF;
      result |= diffOfDigits << shift;

      shift += 4;
      bcdFirst >>= shift;
      bcdSecond >>= shift;
    }

    return new BCDResult(result, borrow);
  }

  public BCDResult bcdSubtract16Bit(int bcdFirst, int bcdSecond, boolean borrow)
  {
    int result = 0;
    int shift = 0;
    while (shift < 16)
    {
      int digitOfFirst = (bcdFirst & 0xFF);
      int digitOfSecond = (bcdSecond & 0xFF);
      BCDResult bcd8BitResult = bcdSubtract8Bit(digitOfFirst, digitOfSecond, borrow);
      borrow = bcd8BitResult.carry;
      int partialResult = bcd8BitResult.value;
      result = toShort(result | (partialResult << shift));
      shift += 8;
      bcdFirst = toShort(bcdFirst >> shift);
      bcdSecond = toShort(bcdSecond >> shift);
    }
    return new BCDResult(result, borrow);
  }

  protected void execute8BitADC()
  {
    int operand = getData();
    int accumulator = getA();
    int carryValue = getCarry();

    int result = accumulator + operand + carryValue;

    accumulator &= 0x7F;
    operand &= 0x7F;
    boolean carryOutOfPenultimateBit = isMemoryNegative(trimMemory(accumulator + operand + carryValue));

    boolean carry = (result & 0x0100) != 0;
    processorStatus.setCarryFlag(carry);
    processorStatus.setOverflowFlag(carry ^ carryOutOfPenultimateBit);
    setA(trimMemory(result));
  }

  protected void execute16BitADC()
  {
    int operand = getData();
    int accumulator = getA();
    int carryValue = getCarry();

    int result = accumulator + operand + carryValue;

    accumulator &= 0x7FFF;
    operand &= 0x7FFF;
    boolean carryOutOfPenultimateBit = isMemoryNegative(trimMemory(accumulator + operand + carryValue));

    boolean carry = (result & 0x010000) != 0;
    processorStatus.setCarryFlag(carry);
    processorStatus.setOverflowFlag(carry ^ carryOutOfPenultimateBit);
    setA(trimMemory(result));
  }

  protected void execute8BitBCDADC()
  {
    int operand = getDataLow();
    int accumulator = getA();

    BCDResult bcdResult = bcdAdd8Bit(operand, accumulator, isCarrySet());
    processorStatus.setCarryFlag(bcdResult.carry);
    setA(bcdResult.value);
  }

  protected void execute16BitBCDADC()
  {
    int operand = getData();
    int accumulator = getA();

    BCDResult bcdResult = bcdAdd16Bit(operand, accumulator, isCarrySet());
    processorStatus.setCarryFlag(bcdResult.carry);
    setA(bcdResult.value);
  }

  public void execute8BitSBC()
  {
    int value = getData();
    int accumulator = getA();
    int borrowValue = 1 - getCarry();

    int result = accumulator - value - borrowValue;

    accumulator &= 0x7F;
    value &= 0x7F;
    boolean borrowFromPenultimateBit = isMemoryNegative(trimMemory(accumulator - value - borrowValue));
    boolean borrow = (result & 0x0100) != 0;

    processorStatus.setCarryFlag(!borrow);
    processorStatus.setOverflowFlag(borrow ^ borrowFromPenultimateBit);
    setA(trimMemory(result));
  }

  protected void execute16BitSBC()
  {
    int value = getData();
    int accumulator = getA();
    int borrowValue = 1 - getCarry();

    int result = accumulator - value - borrowValue;

    accumulator &= 0x7FFF;
    value &= 0x7FFF;
    boolean borrowFromPenultimateBit = isMemoryNegative(trimMemory(accumulator - value - borrowValue));
    boolean borrow = (result & 0x010000) != 0;

    processorStatus.setCarryFlag(!borrow);
    processorStatus.setOverflowFlag(borrow ^ borrowFromPenultimateBit);
    setA(trimMemory(result));
  }

  protected void execute8BitBCDSBC()
  {
    int value = getDataLow();
    int accumulator = getLowByte(getA());

    BCDResult bcdResult = bcdSubtract8Bit(value, accumulator, !isCarrySet());
    processorStatus.setCarryFlag(!bcdResult.carry);
    setA(bcdResult.value);
  }

  protected void execute16BitBCDSBC()
  {
    int value = getData();
    int accumulator = getA();

    BCDResult bcdResult = bcdSubtract16Bit(value, accumulator, !isCarrySet());
    processorStatus.setCarryFlag(!bcdResult.carry);
    setA(bcdResult.value);
  }

  public boolean blockMoveNext()
  {
    accumulator = toShort(accumulator--);
    xIndex = trimIndex(xIndex++);
    yIndex = trimIndex(yIndex++);

    return accumulator != 0xffff;
  }

  public boolean blockMovePrevious()
  {
    accumulator = toShort(accumulator--);
    xIndex = trimIndex(xIndex--);
    yIndex = trimIndex(yIndex--);

    return accumulator != 0xffff;
  }

  private boolean isMemoryNegative(int operand)
  {
    if (isMemory16Bit())
    {
      return is16bitValueNegative(operand);
    }
    else
    {
      return is8bitValueNegative(operand);
    }
  }

  private int trimMemory(int value)
  {
    if (isMemory16Bit())
    {
      value = toShort(value);
    }
    else
    {
      value = toByte(value);
    }
    return value;
  }

  private int trimIndex(int value)
  {
    if (isIndex16Bit())
    {
      value = toShort(value);
    }
    else if (isIndex8Bit())
    {
      value = toByte(value);
    }
    return value;
  }

  private void branch(boolean condition)
  {
    if (!condition)
    {
      doneInstruction();
    }
  }

  public void ASL()
  {
    int operand = getData();
    boolean carry = isMemoryNegative(operand);
    operand = trimMemory(operand << 1);
    setCarryFlag(carry);
    setData(operand);
  }

  public void ASL_A()
  {
    int operand = getA();
    boolean carry = isMemoryNegative(operand);
    operand = trimMemory(operand << 1);
    setCarryFlag(carry);
    setA(operand);
  }

  public void PER()
  {
    internalData = toShort(internalData + programAddress.getOffset());  // + Carry?
  }

  public void PLD()
  {
    setDirectPage(getData16Bit());
  }

  public void PHD()
  {
    internalData = directPage;
  }

  public void PLB()
  {
    setDataBank(getDataLow());
  }

  public void PHB()
  {
    setDataLow(getDataBank());
  }

  public void PHK()
  {
    setDataLow(getProgramCounter().getBank());
  }

  public void PLP()
  {
    processorStatus.setRegisterValue(getDataLow());
    processorStatusChanged();
  }

  public void PHP()
  {
    setDataLow(processorStatus.getRegisterValue());
  }

  public void BRK()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
    if (processorStatus.isEmulation())
    {
      processorStatus.setBreakFlag(true);
    }
  }

  public void COP()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
    if (processorStatus.isEmulation())
    {
      processorStatus.setBreakFlag(true);
    }
  }

  public void ORA()
  {
    setA(getA() | getData());
  }

  public void TSB()
  {
    int value = getData();
    setData((value | getA()), false);
    processorStatus.setZeroFlag((value & getA()) == 0);
  }

  public void TRB()
  {
    int value = getData();
    setData(value & trimMemory(~getA()), false);
    processorStatus.setZeroFlag((value & getA()) == 0);
  }

  public void BPL()
  {
    branch(!isSignSet());
  }

  public void BMI()
  {
    branch(isSignSet());
  }

  public void CLC()
  {
    setCarryFlag(false);
  }

  public void INC_A()
  {
    incrementA();
  }

  public void TCS()
  {
    int stackPointer = getStackPointer();
    if (processorStatus.isEmulation())
    {
      stackPointer = setLowByte(stackPointer, getLowByte(getA()));
    }
    else
    {
      stackPointer = getA();
    }
    setStackPointer(stackPointer);
  }

  public void AND()
  {
    setA((getA() & getData()));
  }

  public void BIT()
  {
    int value = getData();

    if (isMemory16Bit())
    {
      processorStatus.setNegativeFlag(is16bitValueNegative(value));
      processorStatus.setOverflowFlag((value & 0x4000) != 0);
      processorStatus.setZeroFlagFrom16BitValue((value & getA()));
    }
    else
    {
      processorStatus.setNegativeFlag(is8bitValueNegative(value));
      processorStatus.setOverflowFlag((value & 0x40) != 0);
      processorStatus.setZeroFlagFrom8BitValue((value & getA()));
    }
  }

  public void BIT_I()
  {
    if (isMemory16Bit())
    {
      processorStatus.setZeroFlagFrom16BitValue((getData() & getA()));
    }
    else
    {
      processorStatus.setZeroFlagFrom8BitValue((getData() & getA()));
    }
  }

  public void ROL()
  {
    setData(rotateLeft(getData()));
  }

  public void ROL_A()
  {
    setA(rotateLeft(getA()));
  }

  public void SEC()
  {
    processorStatus.setCarryFlag(true);
  }

  public void DEC_A()
  {
    decrementA();
  }

  public void TSC()
  {
    setA(getStackPointer());
    setSignAndZeroFromMemory(getA());
  }

  public void RTI()
  {
    processorStatus.setRegisterValue(getDataLow());
  }

  public void EOR()
  {
    int result = trimMemory(getA() ^ getData());
    setSignAndZeroFromMemory(result);
    setA(result);
  }

  public void WDM()
  {
  }

  public void MVP()
  {
    if (!blockMovePrevious())
    {
      doneInstruction();
    }
  }

  public void MVN()
  {
    if (!blockMoveNext())
    {
      doneInstruction();
    }
  }

  public void LSR()
  {
    setData(shiftRight(getData()));
  }

  public void PHA()
  {
    setData(getA(), false);
  }

  public void LSR_A()
  {
    setA(shiftRight(getA()));
  }

  public void BVC()
  {
    branch(!isOverflowSet());
  }

  public void CLI()
  {
    processorStatus.setInterruptDisableFlag(false);
  }

  public void PHY()
  {
    setIndexData(getY(), false);
  }

  public void TCD()
  {
    setDirectPage(accumulator);
    processorStatus.setSignAndZeroFlagFrom16BitValue(accumulator);
  }

  public void ADC()
  {
    if (isMemory16Bit())
    {
      if (!processorStatus.isDecimal())
      {
        execute16BitADC();
      }
      else
      {
        execute16BitBCDADC();
      }
    }
    else
    {
      if (!processorStatus.isDecimal())
      {
        execute8BitADC();
      }
      else
      {
        execute8BitBCDADC();
      }
    }
  }

  public void STZ()
  {
    setData(0);
  }

  public void PLA()
  {
    setA(getData());
  }

  public void ROR()
  {
    setData(rotateRight(getData()));
  }

  public void ROR_A()
  {
    setA(rotateRight(getA()));
  }

  public void BVS()
  {
    branch(processorStatus.isOverflowFlag());
  }

  public void SEI()
  {
    processorStatus.setInterruptDisableFlag(true);
  }

  public void PLY()
  {
    setY(getIndexData());
  }

  public void TDC()
  {
    setC(getDirectPage());
  }

  public void BRA()
  {
    branch(true);
  }

  public void STA()
  {
    setData(getA(), false);
  }

  public void STY()
  {
    setIndexData(getY(), false);
  }

  public void STX()
  {
    setIndexData(getX(), false);
  }

  public void DEY()
  {
    decrementY();
  }

  public void TXA()
  {
    if (isMemory8Bit() && isIndex16Bit())
    {
      setA(getLowByte(getX()));
    }
    else if (isMemory16Bit() && isIndex8Bit())
    {
      setA(setLowByte(getA(), getX()));
    }
    else
    {
      setA(getX());
    }
  }

  public void BCC()
  {
    branch(!getCpuStatus().isCarry());
  }

  public void LDY()
  {
    if (isIndex16Bit())
    {
      setY(internalData);
    }
    else
    {
      setY(toByte(internalData));
    }
  }

  public void LDA()
  {
    setA(getData());
  }

  public void LDX()
  {
    if (isIndex16Bit())
    {
      setX(internalData);
    }
    else
    {
      setX(toByte(internalData));
    }
  }

  public void BCS()
  {
    branch(processorStatus.isCarry());
  }

  public void CLV()
  {
    processorStatus.setOverflowFlag(false);
  }

  public void TSX()
  {
    int stackPointer = getStackPointer();
    if (isIndex8Bit())
    {
      int stackPointerLower8Bits = getLowByte(stackPointer);
      setX(setLowByte(getX(), stackPointerLower8Bits));
    }
    else
    {
      setX(stackPointer);
    }
  }

  public void TYX()
  {
    setX(getY());
  }

  public void TYA()
  {
    if (isMemory8Bit() && isIndex16Bit())
    {
      setA(getLowByte(getY()));
    }
    else if (isMemory16Bit() && isIndex8Bit())
    {
      setA(setLowByte(getA(), getY()));
    }
    else
    {
      setA(getY());
    }
  }

  public void TXS()
  {
    if (isEmulationMode())
    {
      int newStackPointer = 0x100 | getLowByte(getX());
      setStackPointer(newStackPointer);
    }
    else
    {
      setStackPointer(getX());
    }
  }

  public void TXY()
  {
    setY(getX());
  }

  public void CPY()
  {
    int value = getIndexData();
    setSignAndZeroFromIndex(trimMemory(getY() - value));
    setCarryFlag(getY() >= value);
  }

  public void CMP()
  {
    int value = getData();
    setSignAndZeroFromMemory(trimMemory(getA() - value));
    setCarryFlag(getA() >= value);
  }

  public void REP()
  {
    int value = toByte(~getDataLow());
    processorStatus.setRegisterValue(processorStatus.getRegisterValue() & value);
    processorStatusChanged();
  }

  public void TAY()
  {
    if ((isMemory8Bit() && isIndex8Bit()) ||
        (isMemory16Bit() && isIndex8Bit()))
    {
      int lower8BitsOfA = getLowByte(getA());
      setY(setLowByte(getY(), lower8BitsOfA));
    }
    else
    {
      setY(getA());
    }
  }

  public void TAX()
  {
    if ((isMemory8Bit() && isIndex8Bit()) ||
        (isMemory16Bit() && isIndex8Bit()))
    {
      int lower8BitsOfA = getLowByte(getA());
      setX(setLowByte(getX(), lower8BitsOfA));
    }
    else
    {
      setX(getA());
    }
  }

  public void PHX()
  {
    setIndexData(getX(), false);
  }

  public void STP()
  {
    stopped = true;
  }

  public void DEC()
  {
    setData(trimMemory(getData() - 1));
  }

  public void INY()
  {
    incrementY();
  }

  public void DEX()
  {
    decrementX();
  }

  public void WAI()
  {
  }

  public void BNE()
  {
    branch(!processorStatus.isZeroFlag());
  }

  public void CLD()
  {
    processorStatus.setDecimalFlag(false);
  }

  public void CPX()
  {
    int value = getIndexData();
    setSignAndZeroFromIndex(trimMemory(getX() - value));
    setCarryFlag(getX() >= value);
  }

  public void SBC()
  {
    if (isMemory16Bit())
    {
      if (!processorStatus.isDecimal())
      {
        execute16BitSBC();
      }
      else
      {
        execute16BitBCDSBC();
      }
    }
    else
    {
      if (!processorStatus.isDecimal())
      {
        execute8BitSBC();
      }
      else
      {
        execute8BitBCDSBC();
      }
    }
  }

  public void SEP()
  {
    int value = getDataLow();
    if (isEmulationMode())
    {
      value = value & 0xCF;
    }
    processorStatus.setRegisterValue(processorStatus.getRegisterValue() | value);
    processorStatusChanged();
  }

  public void INC()
  {
    setData(trimMemory(getData() + 1));
  }

  public void INX()
  {
    incrementX();
  }

  public void NOP()
  {

  }

  public void XBA()
  {
    int lowerA = IntUtil.getLowByte(getA());
    int higherA = IntUtil.getHighByte(getA());
    accumulator = higherA | (lowerA << 8);
  }

  public void BEQ()
  {
    branch(processorStatus.isZeroFlag());
  }

  public void SED()
  {
    processorStatus.setDecimalFlag(true);
  }

  public void PLX()
  {
    setX(getIndexData());
  }

  public void XCE()
  {
    boolean oldCarry = processorStatus.isCarry();
    boolean oldEmulation = processorStatus.isEmulation();
    setEmulationMode(oldCarry);
    processorStatus.setCarryFlag(oldEmulation);

    processorStatus.setAccumulatorWidthFlag(processorStatus.isEmulation());
    processorStatus.setIndexWidthFlag(processorStatus.isEmulation());
  }

  public void ABORT()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
  }

  public void IRQ()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
  }

  public void NMI()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
  }

  public void RES()
  {
    setEmulationMode(true);
    dataBank = 0;
    directPage = 0;
    programAddress.setBank(0);
    processorStatus.setDecimalFlag(false);
    processorStatus.setInterruptDisableFlag(true);
  }

  public String getByteStringHex(int value)
  {
    return "0x" + to8BitHex(value);
  }

  private String getWordStringHex(int value)
  {
    return "0x" + to16BitHex(value);
  }

  public String getAddressValueHex()
  {
    return getByteStringHex(getAddress().getBank()) + ":" + to16BitHex(getAddress().getOffset());
  }

  public String getAccumulatorValueHex()
  {
    return getWordStringHex(getA());
  }

  public String getXValueHex()
  {
    return getWordStringHex(getX());
  }

  public String getYValueHex()
  {
    return getWordStringHex(getY());
  }

  public String getStackValueHex()
  {
    return getWordStringHex(getStackPointer());
  }

  public String getProgramCounterValueHex()
  {
    return getByteStringHex(getProgramCounter().getBank()) + ":" + to16BitHex(getProgramCounter().getOffset());
  }

  public String getDataValueHex()
  {
    return getWordStringHex(getData16Bit());
  }

  public String getOpcodeValueHex()
  {
    if (getCycle() != 0)
    {
      int code = getOpCode().getCode();
      if (code >= 0 && code <= 255)
      {
        return getByteStringHex(code);
      }
      else
      {
        return "---";
      }
    }
    else
    {
      return "###";
    }
  }

  public String getOpcodeMnemonicString()
  {
    return getOpCode().getName();
  }

  private String getStatusString()
  {
    String z = processorStatus.isZeroFlag() ? "Z:1 " : "Z:0 ";
    String n = processorStatus.isNegative() ? "N:1 " : "N:0 ";
    String d = processorStatus.isDecimal() ? "D:1 " : "D:0 ";
    String i = processorStatus.isInterruptDisable() ? "I:1 " : "I:0 ";
    String m = processorStatus.isAccumulator8Bit() ? "M8  " : "M16 ";
    String x = "";
    if (!isEmulationMode())
    {
      x = processorStatus.isIndex8Bit() ? "X8  " : "X16 ";
    }
    String c = processorStatus.isCarry() ? "C1 " : "C0 ";
    String e = processorStatus.isEmulation() ? "E1 " : "E0 ";
    String o = processorStatus.isOverflowFlag() ? "O1 " : "O0 ";
    String b = "";
    if (processorStatus.isEmulation())
    {
      b = processorStatus.isBreak() ? "B1 " : "B0 ";
    }
    return z + n + d + i + m + x + c + e + o + b;
  }

  public void dump()
  {
    if (previousClock)
    {
      System.out.println("  --- Internal Status ---- ");
      System.out.println("       Op-Code: " + getOpcodeValueHex());
      System.out.println("      Mnemonic: " + getOpcodeMnemonicString());
      System.out.println("         Cycle: " + getCycle() + (previousClock ? " (High)" : " (Low)"));
      System.out.println("       Address: " + getAddressValueHex());
      System.out.println("          Data: " + getDataValueHex());
      System.out.println("   Accumulator: " + getAccumulatorValueHex());
      System.out.println("       X-Index: " + getXValueHex());
      System.out.println("       Y-Index: " + getYValueHex());
      System.out.println(" Stack Pointer: " + getStackValueHex());
      System.out.println(" Program Count: " + getProgramCounterValueHex());
      System.out.println("        Status: " + getStatusString());
      System.out.println("Address Offset: " + getOpCode().getCycles().getBusCycle(getCycle()).toAddressOffsetString());
      System.out.println("     Operation: " + getOpCode().getCycles().getBusCycle(getCycle()).toOperationString());
      System.out.println();
    }
  }
}

