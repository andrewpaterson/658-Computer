package name.bizna.emu65816;

import name.bizna.emu65816.opcode.*;

import static name.bizna.emu65816.Binary.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class Cpu65816
{
  // Status register
  protected CpuStatus processorStatus;
  protected Pins pins;
  protected boolean stopped;

  protected int accumulator;
  protected int xIndex;
  protected int yIndex;
  protected int dataBank;
  protected int directPage;
  protected Address programAddress;
  protected int stackPointer;

  // Total number of cycles
  protected long totalCyclesCounter;

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

  protected int pinData;  //Think about this very carefully.

  //These are not the values on the pins, they are internal data.
  protected boolean internalRead;
  protected boolean internalValidProgramAddress;
  protected boolean internalValidDataAddress;
  protected Address internalAddress;
  protected int internalData;
  protected int internalDirectOffset;
  protected int relativeOffset;
  protected Address internalProgramCounter;

  public Cpu65816(Pins pins)
  {
    programAddress = new Address(0x00, 0x0000);
    setStackPointer();
    opCodeTable = OpCodeTable.createTable();
    resetOpcode = new OpCode_RES();
    irqOpcode = new OpCode_IRQ();
    nmiOpcode = new OpCode_NMI();
    abortOpcode = new OpCode_ABORT();
    fetchNextOpcode = new OpCode_FetchNextOpCode();
    this.pins = pins;
    totalCyclesCounter = 0;
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
    pinData = 0;
    internalData = 0;
    internalDirectOffset = 0;
    relativeOffset = 0;
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
  }

  public void setA(int accumulator)
  {
    if (isMemory16Bit())
    {
      assert16Bit(accumulator, "Accumulator");
      processorStatus.updateSignAndZeroFlagFrom16BitValue(accumulator);
      this.accumulator = accumulator;
    }
    else
    {
      assert8Bit(accumulator, "Accumulator");
      processorStatus.updateSignAndZeroFlagFrom8BitValue(accumulator);
      this.accumulator = setLowByte(this.accumulator, accumulator);
    }
  }

  public void setData(int data, boolean updateFlags)
  {
    if (isMemory16Bit())
    {
      assert16Bit(data, "Data");
      if (updateFlags)
      {
        processorStatus.updateSignAndZeroFlagFrom16BitValue(data);
      }
      internalData = data;
    }
    else
    {
      assert8Bit(data, "Data");
      if (updateFlags)
      {
        processorStatus.updateSignAndZeroFlagFrom8BitValue(data);
      }
      internalData = setLowByte(internalData, data);
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
    if (stopped)
    {
      return;
    }

    boolean clock = pins.getPhi2();
    boolean clockFallingEdge = !clock && previousClock;
    boolean clockRisingEdge = clock && !previousClock;
    previousClock = clock;

    if (clockFallingEdge)
    {
      opCode.executeOnFallingEdge(this);

      pins.setRead(internalRead);
      pins.setAddress(internalAddress.getOffset());
      pins.setData(internalAddress.getBank());
      pins.setValidDataAddress(internalValidDataAddress);
      pins.setValidProgramAddress(internalValidProgramAddress);
    }

    if (clockRisingEdge)
    {
      if (!internalRead)
      {
        pins.setData(pinData);
      }
      else
      {
        pinData = pins.getData();
      }
      opCode.executeOnRisingEdge(this);

      nextCycle();
    }
  }

  public void abort()
  {
  }

  public void interruptRequest()
  {
  }

  public void nonMaskableInterrupt()
  {
  }

  public boolean isMemory8Bit()
  {
    if (processorStatus.isEmulationMode())
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
    if (processorStatus.isEmulationMode())
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

  public void addToCycles(int cycles)
  {
    totalCyclesCounter += cycles;
  }

  public void addToProgramAddress(int bytes)
  {
    programAddress.offset(bytes, false);
  }

  public void addToProgramAddressAndCycles(int bytes, int cycles)
  {
    addToCycles(cycles);
    addToProgramAddress(bytes);
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

  public void setProgramAddressHigh(int data)
  {
    assert8Bit(data, "Program Address High");
    programAddress.setOffsetHigh(data);
  }

  public void setProgramAddressLow(int data)
  {
    assert8Bit(data, "Program Address Low");
    programAddress.setOffsetLow(data);
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

  public int getRelativeOffset()
  {
    return relativeOffset;
  }

  public int getDirectPage()
  {
    return directPage;
  }

  public int getDirectOffset()
  {
    return internalDirectOffset;
  }

  public void setStackPointer()
  {
    stackPointer = 0x01FF;
  }

  public int getStackPointer()
  {
    if (!processorStatus.isEmulationMode())
    {
      return stackPointer;
    }
    else
    {
      return getLowByte(stackPointer) | 0x100;
    }
  }

  public void stop()
  {
    stopped = true;
  }

  public boolean isStopped()
  {
    return stopped;
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

  public void readData(Address address)
  {
    this.internalAddress = address;
    this.internalRead = true;
    this.internalValidProgramAddress = false;
    this.internalValidDataAddress = true;
  }

  public void readOpCode(Address address)
  {
    this.internalAddress = address;
    this.internalRead = true;
    this.internalValidProgramAddress = true;
    this.internalValidDataAddress = true;
  }

  public void writeData(Address address)
  {
    this.internalAddress = address;
    this.internalRead = false;
    this.internalValidProgramAddress = false;
    this.internalValidDataAddress = true;
  }

  public void noAddress()
  {
    this.internalAddress = programAddress;
    this.internalRead = true;
    this.internalValidProgramAddress = false;
    this.internalValidDataAddress = false;
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

  public boolean isClockLow()
  {
    return !pins.getPhi2();
  }

  public boolean isClockHigh()
  {
    return pins.getPhi2();
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

  public void setRead(boolean read)
  {
    this.internalRead = read;
  }

  public void incrementProgramAddress()
  {
    this.programAddress.offset(Sizeof.sizeofByte, true);
  }

  public void decrementProgramCounter()
  {
    this.programAddress.offset(-Sizeof.sizeofByte, true);
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

  public boolean isRead()
  {
    return internalRead;
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

  public void setRelativeOffsetLow(int data)
  {
    assert8Bit(data, "Relative Offset Low");
    relativeOffset = setLowByte(relativeOffset, data);
  }

  public void setRelativeOffsetHigh(int data)
  {
    assert8Bit(data, "Relative Offset High");
    relativeOffset = setHighByte(relativeOffset, data);
  }

  public void clearRelativeOffsetHigh()
  {
    relativeOffset = toByte(relativeOffset);
  }

  public void setStackPointer(int data)
  {
    assert16Bit(data, "Stack Pointer");
    stackPointer = data;
  }

  public void setStackPointerLow(int data)
  {
    assert8Bit(data, "Stack Pointer Low");
    stackPointer = setLowByte(stackPointer, data);
  }

  public void setStackPointerHigh(int data)
  {
    assert8Bit(data, "Stack Pointer High");
    stackPointer = setHighByte(stackPointer, data);
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

  public void setPinsData(int data)
  {
    pins.setData(data);
  }

  public void setPinsAddress(int address)
  {
    pins.setAddress(address);
  }

  public void setEmulationMode(boolean emulation)
  {
    processorStatus.setEmulationFlag(emulation);
    if (emulation)
    {
      xIndex = getLowByte(xIndex);
      yIndex = getLowByte(yIndex);
      processorStatus.setAccumulatorWidthFlag(true);
      processorStatus.setIndexWidthFlag(true);
    }
  }

  public boolean isEmulationMode()
  {
    return false;
  }

  public void setCarryFlag(boolean carry)
  {
    processorStatus.setCarryFlag(carry);
  }

  public void incrementA()
  {
    int a = getA();
    a++;
    a = trimMemory(a);
    setA(a);
  }

  public void incrementX()
  {
    int x = getX();
    x++;
    x = trimIndex(x);
    setX(x);
  }

  public void incrementY()
  {
    int y = getY();
    y++;
    y = trimIndex(y);
    setY(y);
  }

  public void decrementY()
  {
    int y = getY();
    y--;
    y = trimIndex(y);
    setY(y);
  }

  public void decrementA()
  {
    int a = getA();
    a--;
    a = trimMemory(a);
    setA(a);
  }

  public void decrementX()
  {
    int x = getX();
    x--;
    x = trimIndex(x);
    setX(x);
  }

  public void incrementData()
  {
    int operand = getData();
    operand++;
    operand = trimMemory(operand);
    setData(operand);
  }

  public void decrementData()
  {
    int operand = getData();
    operand--;
    operand = trimMemory(operand);
    setData(operand);
  }

  public void ASL()
  {
    int operand = getData();
    boolean carry = isNegative(operand);
    operand = trimMemory(operand << 1);
    setCarryFlag(carry);
    setData(operand);
  }

  public void ASL_A()
  {
    int operand = getA();
    boolean carry = isNegative(operand);
    operand = trimMemory(operand << 1);
    setCarryFlag(carry);
    setA(operand);
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

  private boolean isNegative(int operand)
  {
    if (isMemory16Bit())
    {
      return is16bitValueNegative(operand);
    }
    else if (isMemory8Bit())
    {
      return is8bitValueNegative(operand);
    }
    return false;
  }

  private int trimMemory(int a)
  {
    if (isMemory16Bit())
    {
      a = toShort(a);
    }
    else if (isMemory8Bit())
    {
      a = toByte(a);
    }
    return a;
  }

  private int trimIndex(int a)
  {
    if (isIndex16Bit())
    {
      a = toShort(a);
    }
    else if (isIndex8Bit())
    {
      a = toByte(a);
    }
    return a;
  }

  private void branch()
  {
  }

  public void PER()
  {
    internalData = toShort(internalData + programAddress.getOffset());  // + Carry?
  }

  public void PLD()
  {
    setDirectPage(internalData);
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
    setDataLow(processorStatus.getRegisterValue());
  }

  public void PHP()
  {
    processorStatus.setRegisterValue(getDataLow());
  }

  public void BRK()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
    if (processorStatus.isEmulationMode())
    {
      processorStatus.setBreakFlag(true);
    }
  }

  public void COP()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
    if (processorStatus.isEmulationMode())
    {
      processorStatus.setBreakFlag(true);
    }
  }

  public void ORA()
  {
    int operand = getData();
    setA(getA() | operand);
  }

  public void TSB()
  {
    int value = getData();
    setData((value | getA()), false);
    processorStatus.setZeroFlag((value & getA()) == 0);
  }

  public  void TRB()
  {
    int value = getData();
    setData(value & trimMemory(~getA()), false);
    processorStatus.setZeroFlag((value & getA()) == 0);
  }

  public void BPL()
  {
    if (!processorStatus.signFlag())
    {
      branch();
    }
    else
    {
      doneInstruction();
    }
  }

  public void BMI()
  {

  }

  public void CLC()
  {

  }

  public void INC_A()
  {

  }

  public void TCS()
  {

  }

  public void AND()
  {

  }

  public void BIT()
  {

  }

  public void ROL()
  {

  }

  public void ROL_A()
  {

  }

  public void SEC()
  {

  }

  public void DEC_A()
  {

  }

  public void TSC()
  {

  }

  public void RTI()
  {

  }

  public void EOR()
  {

  }

  public void WDM()
  {

  }

  public void MVP()
  {

  }

  public void MVN()
  {

  }

  public void LSR()
  {

  }

  public void PHA()
  {

  }

  public void LSR_A()
  {

  }

  public void BVC()
  {

  }

  public void CLI()
  {

  }

  public void PHY()
  {

  }

  public void TCD()
  {

  }

  public void ADC()
  {

  }

  public void STZ()
  {

  }

  public void PLA()
  {

  }

  public void ROR()
  {

  }

  public void ROR_A()
  {

  }

  public void BVS()
  {

  }

  public void SEI()
  {

  }

  public void PLY()
  {

  }

  public void TDC()
  {

  }

  public void BRA()
  {

  }

  public void STA()
  {

  }

  public void STY()
  {

  }

  public void STX()
  {

  }

  public void DEY()
  {

  }

  public void TXA()
  {

  }

  public void BCC()
  {

  }

  public void LDY()
  {

  }

  public void LDA()
  {
  }

  public void LDX()
  {

  }

  public void BCS()
  {

  }

  public void CLV()
  {

  }

  public void TSX()
  {

  }

  public void TYX()
  {

  }

  public void TYA()
  {

  }

  public void TXS()
  {

  }

  public void TXY()
  {

  }

  public void CPY()
  {

  }

  public void CMP()
  {

  }

  public void REP()
  {

  }

  public void TAY()
  {

  }

  public void TAX()
  {

  }

  public void PHX()
  {

  }

  public void STP()
  {

  }

  public void DEC()
  {

  }

  public void INY()
  {

  }

  public void DEX()
  {

  }

  public void WAI()
  {

  }

  public void BNE()
  {

  }

  public void CLD()
  {

  }

  public void CPX()
  {

  }

  public void SBC()
  {

  }

  public void SEP()
  {

  }

  public void INC()
  {

  }

  public void INX()
  {

  }

  public void NOP()
  {

  }

  public void XBA()
  {

  }

  public void BEQ()
  {

  }

  public void SED()
  {
    processorStatus.setDecimalFlag(true);
  }

  public void PLX()
  {

  }

  public void XCE()
  {

  }

  public void RES()
  {
    setEmulationMode(true);
    dataBank = 0;
    directPage = 0;
    programAddress.setBank(0);
    stackPointer = getLowByte(stackPointer) | 0x100;
    processorStatus.setDecimalFlag(false);
    processorStatus.setInterruptDisableFlag(true);
  }
}

