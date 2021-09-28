package name.bizna.emu65816;

import name.bizna.emu65816.opcode.*;

import static name.bizna.emu65816.Binary.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class Cpu65816
{
  protected EmulationModeInterrupts mEmulationInterrupts;
  protected NativeModeInterrupts mNativeInterrupts;

  // Status register
  protected CpuStatus mCpuStatus;
  protected Pins mPins;
  protected boolean mStopped;

  protected int accumulator;
  protected int xIndex;
  protected int yIndex;
  protected int dataBank;
  protected int directPage;
  protected Address mProgramAddress;
  protected Address mStackAddress;

  // Total number of cycles
  protected long mTotalCyclesCounter;

  protected boolean previousClock;
  protected int cycle;
  protected OpCode opCode;

  // Real OpCode Table.
  protected static OpCode[] opCodeTable;
  protected static OpCode resetOpcode;
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

  public Cpu65816(Pins pins)
  {
    mEmulationInterrupts = new EmulationModeInterrupts();
    mNativeInterrupts = new NativeModeInterrupts();
    mProgramAddress = new Address(0x00, 0x0000);
    mStackAddress = new Address(0x00, 0x01FF);
    opCodeTable = OpCodeTable.createTable();
    resetOpcode = new Reset("Reset", -1, AddressingMode.Interrupt);
    irqOpcode = new InterruptRequest("IRQ", -1, AddressingMode.Interrupt);
    nmiOpcode = new NonMaskableInterrupt("NMI", -1, AddressingMode.Interrupt);
    fetchOpcode = new FetchOpCode("Fetch Opcode", -1, AddressingMode.Implied);
    mPins = pins;
    mTotalCyclesCounter = 0;
    accumulator = 0;
    xIndex = 0;
    yIndex = 0;
    dataBank = 0;
    directPage = 0;
    mCpuStatus = new CpuStatus();
    mStopped = false;
    previousClock = true;
    cycle = 1;
    opCode = resetOpcode;
    pinData = 0;
    internalData = 0;
    internalDirectOffset = 0;
    internalImmediate = 0;
    internalStackOffset = 0;
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
    if (isAccumulator16Bit())
    {
      assert16Bit(accumulator, "Accumulator");
    }
    else
    {
      assert8Bit(accumulator, "Accumulator");
    }
    this.accumulator = accumulator;
  }

  public int getA()
  {
    if (isAccumulator16Bit())
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
    return mProgramAddress;
  }

  public CpuStatus getCpuStatus()
  {
    return mCpuStatus;
  }

  public void tick()
  {
    if (mStopped)
    {
      return;
    }

    boolean clock = mPins.getPhi2();
    boolean clockFallingEdge = !clock && previousClock;
    boolean clockRisingEdge = clock && !previousClock;
    previousClock = clock;

    if (clockFallingEdge)
    {
      opCode.executeOnFallingEdge(this);

      mPins.setRead(internalRead);
      mPins.setAddress(internalAddress.getOffset());
      mPins.setData(internalAddress.getBank());
      mPins.setValidDataAddress(internalValidDataAddress);
      mPins.setValidProgramAddress(internalValidProgramAddress);
    }

    if (clockRisingEdge)
    {
      if (!internalRead)
      {
        mPins.setData(pinData);
      }
      else
      {
        pinData = mPins.getData();
      }
      opCode.executeOnRisingEdge(this);

      nextCycle();
    }
  }

//  public void executeNextInstruction()
//  {
//    if ((mStopped))
//    {
//      return;
//    }
//
//    if (opCode.isReset())
//    {
//      return;
//    }

//    if ((mPins.IRQ) && (!mCpuStatus.interruptDisableFlag()))
//    {
//        /*
//            The program bank register (PB, the A16-A23 part of the address bus) is pushed onto the hardware stack (65C816/65C802 only when operating in native mode).
//            The most significant byte (MSB) of the program counter (PC) is pushed onto the stack.
//            The least significant byte (LSB) of the program counter is pushed onto the stack.
//            The status register (SR) is pushed onto the stack.
//            The interrupt disable flag is set in the status register.
//            PB is loaded with $00 (65C816/65C802 only when operating in native mode).
//            PC is loaded from the relevant vector (see tables).
//        */
//      if (!mCpuStatus.emulationFlag())
//      {
//        mStack.push8Bit(mProgramAddress.getBank());
//        mStack.push16Bit(mProgramAddress.getOffset());
//        mStack.push8Bit(mCpuStatus.getRegisterValue());
//        mCpuStatus.setInterruptDisableFlag(true);
//        mProgramAddress = new Address(readTwoBytes(new Address(mNativeInterrupts.interruptRequest)));
//      }
//      else
//      {
//        mStack.push16Bit(mProgramAddress.getOffset());
//        mStack.push8Bit(mCpuStatus.getRegisterValue());
//        mCpuStatus.setInterruptDisableFlag(true);
//        mProgramAddress = new Address(readTwoBytes(new Address(mEmulationInterrupts.brkIrq)));
//      }
//    }

    // Fetch the instruction
//    int instruction = readByte(mProgramAddress);
//    OpCode opCode = OP_CODE_TABLE[instruction];
//
//    // Execute it
//    opCode.execute(this, 0, false);
//  }

  public boolean isAccumulator8Bit()
  {
    if (mCpuStatus.isEmulationMode())
    {
      return true;
    }
    else
    {
      return mCpuStatus.isAccumulator8Bit();
    }
  }

  public boolean isAccumulator16Bit()
  {
    return !isAccumulator8Bit();
  }

  public boolean isIndex8Bit()
  {
    if (mCpuStatus.isEmulationMode())
    {
      return true;
    }
    else
    {
      return mCpuStatus.isIndex8Bit();
    }
  }

  public boolean isIndex16Bit()
  {
    return !isIndex8Bit();
  }

  public void addToCycles(int cycles)
  {
    mTotalCyclesCounter += cycles;
  }

  public void addToProgramAddress(int bytes)
  {
    mProgramAddress.offset(bytes, false);
  }

  public void addToProgramAddressAndCycles(int bytes, int cycles)
  {
    addToCycles(cycles);
    addToProgramAddress(bytes);
  }

  public void setProgramAddress(Address address)
  {
    mProgramAddress = address;
  }

  public void setProgramAddressBank(int bank)
  {
    assert8Bit(bank, "Program Address Bank");
    mProgramAddress.bank = bank;
  }

  public void setProgramAddressHigh(int data)
  {
    assert8Bit(data, "Program Address High");
    mProgramAddress.setOffsetHigh(data);
  }

  public void setProgramAddressLow(int data)
  {
    assert8Bit(data, "Program Address Low");
    mProgramAddress.setOffsetLow(data);
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

  public Address getAddressOfOpCodeData(AddressingMode addressingMode)
  {
//    int dataAddressBank;
//    int dataAddressOffset;
//
//    switch (addressingMode)
//    {
//      case Interrupt:
//      case Accumulator:
//      case Implied:
//      case StackImplied:
//        // Not really used, doesn't make any sense since these opcodes do not have operands
//        return mProgramAddress;
//
//      case Immediate:
//      case BlockMove:
//        // Blockmove OpCodes have two bytes following them directly
//      case StackAbsolute:
//        // Stack absolute is used to push values following the op code onto the stack
//      case ProgramCounterRelative:
//        // Program counter relative OpCodes such as all branch instructions have an 8 bit operand
//        // following the op code
//      case ProgramCounterRelativeLong:
//        // StackProgramCounterRelativeLong is only used by the PER OpCode, it has 16 bit operand
//      case StackProgramCounterRelativeLong:
//      {
//        dataAddressBank = mProgramAddress.getBank();
//        dataAddressOffset = mProgramAddress.getOffset();
//      }
//      break;
//
//      case Absolute:
//      {
//        dataAddressBank = dataBank;
//        dataAddressOffset = get16BitData(mProgramAddress.newWithOffset1());
//      }
//      break;
//      case AbsoluteLong:
//      {
//        Address address = readLongAddressAt(mProgramAddress.newWithOffset1());
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//
//      case AbsoluteIndirect:
//      {
//        dataAddressBank = mProgramAddress.getBank();
//        Address addressOfOffset = new Address(get16BitData(mProgramAddress.newWithOffset1()));
//        dataAddressOffset = get16BitData();
//      }
//      break;
//      case AbsoluteIndirectLong:
//      {
//        Address addressOfEffectiveAddress = new Address(get16BitData(mProgramAddress.newWithOffset1()));
//        Address address = readLongAddressAt(addressOfEffectiveAddress);
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//      case AbsoluteIndexedIndirectWithX:
//      {
//        Address firstStageAddress = new Address(mProgramAddress.getBank(), get16BitData(mProgramAddress.newWithOffset1()));
//
//        Address secondStageAddress = firstStageAddress.newWithOffsetNoWrapAround(indexWithXRegister());
//        dataAddressBank = mProgramAddress.getBank();
//        dataAddressOffset = get16BitData();
//      }
//      break;
//      case AbsoluteIndexedWithX:
//      {
//        Address firstStageAddress = new Address(dataBank, get16BitData(mProgramAddress.newWithOffset1()));
//        Address address = Address.sumOffsetToAddressNoWrapAround(firstStageAddress, indexWithXRegister());
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//      case AbsoluteLongIndexedWithX:
//      {
//        Address firstStageAddress = readLongAddressAt(mProgramAddress.newWithOffset1());
//        Address address = Address.sumOffsetToAddressNoWrapAround(firstStageAddress, indexWithXRegister());
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//      case AbsoluteIndexedWithY:
//      {
//        Address firstStageAddress = new Address(dataBank, get16BitData(mProgramAddress.newWithOffset1()));
//        Address address = Address.sumOffsetToAddressNoWrapAround(firstStageAddress, indexWithYRegister());
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//      case DirectPage:
//      {
//        // Direct page/Zero page always refers to bank zero
//        dataAddressBank = 0x00;
//        if (mCpuStatus.emulationFlag())
//        {
//          dataAddressOffset = get8BitData(mProgramAddress.newWithOffset1());
//        }
//        else
//        {
//          dataAddressOffset = toShort(directPage + get8BitData(mProgramAddress.newWithOffset1()));
//        }
//      }
//      break;
//      case DirectPageIndexedWithX:
//      {
//        dataAddressBank = 0x00;
//        dataAddressOffset = toShort(directPage + indexWithXRegister() + get8BitData(mProgramAddress.newWithOffset1()));
//      }
//      break;
//      case DirectPageIndexedWithY:
//      {
//        dataAddressBank = 0x00;
//        dataAddressOffset = toShort(directPage + indexWithYRegister() + get8BitData(mProgramAddress.newWithOffset1()));
//      }
//      break;
//      case DirectPageIndirect:
//      {
//        Address firstStageAddress = new Address((directPage + get8BitData(mProgramAddress.newWithOffset1())));
//        dataAddressBank = dataBank;
//        dataAddressOffset = get16BitData();
//      }
//      break;
//      case DirectPageIndirectLong:
//      {
//        Address firstStageAddress = new Address((directPage + get8BitData(mProgramAddress.newWithOffset1())));
//        Address address = readLongAddressAt(firstStageAddress);
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//      case DirectPageIndexedIndirectWithX:
//      {
//        Address firstStageAddress = new Address((directPage + get8BitData(mProgramAddress.newWithOffset1()) + indexWithXRegister()));
//        dataAddressBank = dataBank;
//        dataAddressOffset = get16BitData();
//      }
//      break;
//      case DirectPageIndirectIndexedWithY:
//      {
//        Address firstStageAddress = new Address((directPage + get8BitData(mProgramAddress.newWithOffset1())));
//        int secondStageOffset = get16BitData();
//        Address thirdStageAddress = new Address(dataBank, secondStageOffset);
//        Address address = Address.sumOffsetToAddressNoWrapAround(thirdStageAddress, indexWithYRegister());
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//      case DirectPageIndirectLongIndexedWithY:
//      {
//        Address firstStageAddress = new Address((directPage + get8BitData(mProgramAddress.newWithOffset1())));
//        Address secondStageAddress = readLongAddressAt(firstStageAddress);
//        Address address = Address.sumOffsetToAddressNoWrapAround(secondStageAddress, indexWithYRegister());
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//      case StackRelative:
//      {
//        dataAddressBank = 0x00;
//        dataAddressOffset = toShort(getStackPointer() + get8BitData(mProgramAddress.newWithOffset1()));
//      }
//      break;
//      case StackDirectPageIndirect:
//      {
//        dataAddressBank = 0x00;
//        dataAddressOffset = toShort(directPage + get8BitData(mProgramAddress.newWithOffset1()));
//      }
//      break;
//      case StackRelativeIndirectIndexedWithY:
//      {
//        Address firstStageAddress = new Address((getStackPointer() + get8BitData(mProgramAddress.newWithOffset1())));
//        int secondStageOffset = get16BitData();
//        Address thirdStageAddress = new Address(dataBank, secondStageOffset);
//        Address address = Address.sumOffsetToAddressNoWrapAround(thirdStageAddress, indexWithYRegister());
//        dataAddressBank = address.getBank();
//        dataAddressOffset = address.getOffset();
//      }
//      break;
//      default:
//        throw new IllegalStateException("Unexpected value: " + addressingMode);
//    }
//
//    return new Address(dataAddressBank, dataAddressOffset);
    throw new EmulatorException("Not Implemented");
  }

  public int get8BitData()
  {
    return toByte(internalData);
  }

  public int get16BitData()
  {
    return internalData;
  }

  public Address readLongAddressAt(Address address)
  {
    return new Address(0x00, 0);
  }

  public int getDirectPage()
  {
    return directPage;
  }

  public int getDirectOffset()
  {
    return internalDirectOffset;
  }

  public void storeTwoBytes(Address address, int value)
  {
  }

  public void storeByte(Address address, int value)
  {
  }

  public EmulationModeInterrupts getEmulationInterrupts()
  {
    return mEmulationInterrupts;
  }

  public NativeModeInterrupts getNativeInterrupts()
  {
    return mNativeInterrupts;
  }

  public void clearStack()
  {
    mStackAddress = new Address(0x00, 0x01FF);
  }

  public void clearStack(Address address)
  {
    mStackAddress = address;
  }

  public void push8Bit(int value)
  {
    storeByte(mStackAddress, value);
    mStackAddress.offset(Sizeof.sizeofByte, true);
  }

  public void push16Bit(int value)
  {
    push8Bit(getHighByte(value));
    push8Bit(getLowByte(value));
  }

  public int pull8Bit()
  {
    mStackAddress.offset(Sizeof.sizeofByte, true);
    return get8BitData();
  }

  public int pull16Bit()
  {
    return toShort((pull8Bit() | ((pull8Bit()) << 8)));
  }

  public int getStackPointer()
  {
    return mStackAddress.getOffset();
  }

  public void stop()
  {
    mStopped = true;
  }

  public boolean isStopped()
  {
    return mStopped;
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

  public void readProgram(Address address)
  {
    this.internalAddress = address;
    this.internalRead = true;
    this.internalValidProgramAddress = true;
    this.internalValidDataAddress = false;
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
    this.internalAddress = mProgramAddress;
    this.internalRead = true;
    this.internalValidProgramAddress = false;
    this.internalValidDataAddress = false;
  }

  public int getPinData()
  {
    if (mPins.getPhi2())
    {
      return mPins.getData();
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
    return !mPins.getPhi2();
  }

  public boolean isClockHigh()
  {
    return mPins.getPhi2();
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

  public void setRead(boolean read)
  {
    this.internalRead = read;
  }

  public void incrementProgramAddress()
  {
    this.mProgramAddress.offset(Sizeof.sizeofByte, true);
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

  public void setStackPointerOffset(int data)
  {
    assert8Bit(data, "Stack Offset");
    this.internalStackOffset = data;
  }

  public void setDataLow(int data)
  {
    this.data = setLowByte(this.data, data);;
  }

  public void setDataHigh(int data)
  {
    this.data = setHighByte(this.data, data);;
  }
}

