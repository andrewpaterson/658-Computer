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
  protected static OpCode fetchOpcode;

  protected int pinData;  //Think about this very carefully.

  //These are not the values on the pins, they are internal data.
  protected boolean internalRead;
  protected boolean internalValidProgramAddress;
  protected boolean internalValidDataAddress;
  protected Address internalAddress;
  protected int internalData;
  protected int internalDirectOffset;
  protected int internalImmediate;
  protected int internalStackOffset;
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
    fetchOpcode = new FetchOpCode("Fetch Opcode", -1, AddressingMode.Implied);
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
    internalImmediate = 0;
    internalStackOffset = 0;
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

  public void setData(int data)
  {
    if (isMemory16Bit())
    {
      assert16Bit(data, "Data");
      processorStatus.updateSignAndZeroFlagFrom16BitValue(data);
      internalData = data;
    }
    else
    {
      assert8Bit(data, "Data");
      processorStatus.updateSignAndZeroFlagFrom8BitValue(data);
      internalData = setLowByte(internalData, data);
    }
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

  public void reset()
  {
    setEmulationMode(true);
    dataBank = 0;
    directPage = 0;
    programAddress.setBank(0);
    stackPointer = getLowByte(stackPointer) | 0x100;
    processorStatus.setDecimalFlag(false);
    processorStatus.setInterruptDisableFlag(true);
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

  public void break_()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
    if (processorStatus.isEmulationMode())
    {
      processorStatus.setBreakFlag(true);
    }
  }
  public void coprocessor()
  {
    processorStatus.setInterruptDisableFlag(true);
    processorStatus.setDecimalFlag(false);
    if (processorStatus.isEmulationMode())
    {
      processorStatus.setBreakFlag(true);
    }
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

  public int getStackOffset()
  {
    return internalStackOffset;
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
    opCode = fetchOpcode;
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

  public void decrementStackPointer()
  {
    this.stackPointer--;
    stackPointer = toShort(stackPointer);
  }

  public boolean isRead()
  {
    return internalRead;
  }

//  public boolean indexAcrossPage(int offset)
//  {
//    return Address.areOffsetsAreOnDifferentPages(address.getOffset(), address.getOffset() + offset);
//  }

  public void setDirectOffset(int data)
  {
    assert8Bit(data, "Direct Offset");
    internalDirectOffset = data;
  }

  public void setImmediateHigh(int data)
  {
    assert8Bit(data, "Immediate High");
    internalImmediate = setHighByte(internalImmediate, data);
  }

  public void setImmediateLow(int data)
  {
    assert8Bit(data, "Immediate Low");
    internalImmediate = setLowByte(internalImmediate, data);
  }

  public void setStackOffset(int data)
  {
    assert8Bit(data, "Stack Offset");
    internalStackOffset = data;
  }

  public void setDataLow(int data)
  {
    assert8Bit(data, "Data Low");
    internalData = setLowByte(internalData, data);
    ;
  }

  public void setDataHigh(int data)
  {
    assert8Bit(data, "Data High");
    internalData = setHighByte(internalData, data);
    ;
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
}

