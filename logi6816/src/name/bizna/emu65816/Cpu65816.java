package name.bizna.emu65816;

import name.bizna.emu65816.opcode.*;

import static name.bizna.emu65816.Address.sumOffsetToAddress;
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

  protected Address address;
  protected boolean read;
  protected int data;
  protected boolean validProgramAddress;
  protected boolean validDataAddress;

  public Cpu65816(Pins pins)
  {
    mEmulationInterrupts = new EmulationModeInterrupts();
    mNativeInterrupts = new NativeModeInterrupts();
    mProgramAddress = new Address(0x0000);
    mStackAddress = new Address(0x01FF);
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
    address = new Address();
  }

  public void setX(int xIndex)
  {
    this.xIndex = xIndex;
  }

  public void setY(int y)
  {
    yIndex = y;
  }

  public void setA(int accumulator)
  {
    this.accumulator = accumulator;
  }

  public int getA()
  {
    return accumulator;
  }

  public int getX()
  {
    return xIndex;
  }

  public int getY()
  {
    return yIndex;
  }

  public int getDB()
  {
    return dataBank;
  }

  public void setDB(int mDB)
  {
    this.dataBank = toShort(mDB);
  }

  public void setD(int mD)
  {
    this.directPage = toShort(mD);
  }

  public Address getProgramAddress()
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

      mPins.setRead(read);
      mPins.setAddress(address.getOffset());
      mPins.setData(address.getBank());
      mPins.setValidDataAddress(validDataAddress);
      mPins.setValidProgramAddress(validProgramAddress);
    }

    if (clockRisingEdge)
    {
      if (!read)
      {
        mPins.setData(data);
      }
      else
      {
        data = mPins.getData();
      }
      opCode.executeOnRisingEdge(this);

      nextCycle();
    }
  }

  public void executeNextInstruction()
  {
    if ((mStopped))
    {
      return;
    }

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
  }

  public boolean accumulatorIs8BitWide()
  {
    // Accumulator is always 8 bit in emulation mode.
    if (mCpuStatus.emulationFlag())
    {
      return true;
    }
    // Accumulator width set to one means 8 bit accumulator.
    else
    {
      return mCpuStatus.accumulatorWidthFlag();
    }
  }

  public boolean accumulatorIs16BitWide()
  {
    return !accumulatorIs8BitWide();
  }

  public boolean indexIs8BitWide()
  {
    // Index is always 8 bit in emulation mode.
    if (mCpuStatus.emulationFlag())
    {
      return true;
    }
    // Index width set to one means 8 bit accumulator.
    else
    {
      return mCpuStatus.indexWidthFlag();
    }
  }

  public boolean indexIs16BitWide()
  {
    return !indexIs8BitWide();
  }

  public void addToCycles(int cycles)
  {
    mTotalCyclesCounter += cycles;
  }

  public void addToProgramAddress(int bytes)
  {
    mProgramAddress.incrementOffsetBy(bytes);
  }

  public void addToProgramAddressAndCycles(int bytes, int cycles)
  {
    addToCycles(cycles);
    addToProgramAddress(bytes);
  }

  public int indexWithXRegister()
  {
    return (indexIs8BitWide() ? Binary.lower8BitsOf(xIndex) : xIndex);
  }

  public int indexWithYRegister()
  {
    return indexIs8BitWide() ? Binary.lower8BitsOf(yIndex) : yIndex;
  }

  public void setProgramAddress(Address address)
  {
    mProgramAddress = address;
  }

  public void setProgramAddressBank(int bank)
  {
    mProgramAddress.bank = bank;
  }

  public void setProgramAddressHigh(int data)
  {
    mProgramAddress.offset = (mProgramAddress.offset & 0xff) | data << 8;
  }

  public void setProgramAddressLow(int data)
  {
    mProgramAddress.offset = (mProgramAddress.offset & 0xff00) | data;
  }

  public boolean opCodeAddressingCrossesPageBoundary(AddressingMode addressingMode)
  {
    switch (addressingMode)
    {
      case AbsoluteIndexedWithX:
      {
        Address initialAddress = new Address(dataBank, readTwoBytes(mProgramAddress.newWithOffset1()));
        // TODO: figure out when to wrap around and when not to, it should not matter in this case
        // but it matters when fetching data
        Address finalAddress = sumOffsetToAddress(initialAddress, indexWithXRegister());
        return Address.offsetsAreOnDifferentPages(initialAddress.getOffset(), finalAddress.getOffset());
      }
      case AbsoluteIndexedWithY:
      {
        Address initialAddress = new Address(dataBank, readTwoBytes(mProgramAddress.newWithOffset1()));
        // TODO: figure out when to wrap around and when not to, it should not matter in this case
        // but it matters when fetching data
        Address finalAddress = sumOffsetToAddress(initialAddress, indexWithYRegister());
        return Address.offsetsAreOnDifferentPages(initialAddress.getOffset(), finalAddress.getOffset());
      }
      case DirectPageIndirectIndexedWithY:
      {
        int firstStageOffset = toShort(directPage + readByte(mProgramAddress.newWithOffset1()));
        Address firstStageAddress = new Address(firstStageOffset);
        int secondStageOffset = readTwoBytes(firstStageAddress);
        Address thirdStageAddress = new Address(dataBank, secondStageOffset);
        // TODO: figure out when to wrap around and when not to, it should not matter in this case
        // but it matters when fetching data
        Address finalAddress = sumOffsetToAddress(thirdStageAddress, indexWithYRegister());
        return Address.offsetsAreOnDifferentPages(thirdStageAddress.getOffset(), finalAddress.getOffset());
      }

      default:
        throw new IllegalStateException("Unexpected value: " + addressingMode);
    }
  }

  public Address getAddressOfOpCodeData(AddressingMode addressingMode)
  {
    int dataAddressBank;
    int dataAddressOffset;

    switch (addressingMode)
    {
      case Interrupt:
      case Accumulator:
      case Implied:
      case StackImplied:
        // Not really used, doesn't make any sense since these opcodes do not have operands
        return mProgramAddress;

      case Immediate:
      case BlockMove:
        // Blockmove OpCodes have two bytes following them directly
      case StackAbsolute:
        // Stack absolute is used to push values following the op code onto the stack
      case ProgramCounterRelative:
        // Program counter relative OpCodes such as all branch instructions have an 8 bit operand
        // following the op code
      case ProgramCounterRelativeLong:
        // StackProgramCounterRelativeLong is only used by the PER OpCode, it has 16 bit operand
      case StackProgramCounterRelativeLong:
      {
        dataAddressBank = mProgramAddress.getBank();
        dataAddressOffset = mProgramAddress.getOffset();
      }
      break;

      case Absolute:
      {
        dataAddressBank = dataBank;
        dataAddressOffset = readTwoBytes(mProgramAddress.newWithOffset1());
      }
      break;
      case AbsoluteLong:
      {
        Address address = readLongAddressAt(mProgramAddress.newWithOffset1());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;

      case AbsoluteIndirect:
      {
        dataAddressBank = mProgramAddress.getBank();
        Address addressOfOffset = new Address(readTwoBytes(mProgramAddress.newWithOffset1()));
        dataAddressOffset = readTwoBytes(addressOfOffset);
      }
      break;
      case AbsoluteIndirectLong:
      {
        Address addressOfEffectiveAddress = new Address(readTwoBytes(mProgramAddress.newWithOffset1()));
        Address address = readLongAddressAt(addressOfEffectiveAddress);
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case AbsoluteIndexedIndirectWithX:
      {
        Address firstStageAddress = new Address(mProgramAddress.getBank(), readTwoBytes(mProgramAddress.newWithOffset1()));

        Address secondStageAddress = firstStageAddress.newWithOffsetNoWrapAround(indexWithXRegister());
        dataAddressBank = mProgramAddress.getBank();
        dataAddressOffset = readTwoBytes(secondStageAddress);
      }
      break;
      case AbsoluteIndexedWithX:
      {
        Address firstStageAddress = new Address(dataBank, readTwoBytes(mProgramAddress.newWithOffset1()));
        Address address = Address.sumOffsetToAddressNoWrapAround(firstStageAddress, indexWithXRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case AbsoluteLongIndexedWithX:
      {
        Address firstStageAddress = readLongAddressAt(mProgramAddress.newWithOffset1());
        Address address = Address.sumOffsetToAddressNoWrapAround(firstStageAddress, indexWithXRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case AbsoluteIndexedWithY:
      {
        Address firstStageAddress = new Address(dataBank, readTwoBytes(mProgramAddress.newWithOffset1()));
        Address address = Address.sumOffsetToAddressNoWrapAround(firstStageAddress, indexWithYRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case DirectPage:
      {
        // Direct page/Zero page always refers to bank zero
        dataAddressBank = 0x00;
        if (mCpuStatus.emulationFlag())
        {
          dataAddressOffset = readByte(mProgramAddress.newWithOffset1());
        }
        else
        {
          dataAddressOffset = toShort(directPage + readByte(mProgramAddress.newWithOffset1()));
        }
      }
      break;
      case DirectPageIndexedWithX:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = toShort(directPage + indexWithXRegister() + readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case DirectPageIndexedWithY:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = toShort(directPage + indexWithYRegister() + readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case DirectPageIndirect:
      {
        Address firstStageAddress = new Address((directPage + readByte(mProgramAddress.newWithOffset1())));
        dataAddressBank = dataBank;
        dataAddressOffset = readTwoBytes(firstStageAddress);
      }
      break;
      case DirectPageIndirectLong:
      {
        Address firstStageAddress = new Address((directPage + readByte(mProgramAddress.newWithOffset1())));
        Address address = readLongAddressAt(firstStageAddress);
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case DirectPageIndexedIndirectWithX:
      {
        Address firstStageAddress = new Address((directPage + readByte(mProgramAddress.newWithOffset1()) + indexWithXRegister()));
        dataAddressBank = dataBank;
        dataAddressOffset = readTwoBytes(firstStageAddress);
      }
      break;
      case DirectPageIndirectIndexedWithY:
      {
        Address firstStageAddress = new Address((directPage + readByte(mProgramAddress.newWithOffset1())));
        int secondStageOffset = readTwoBytes(firstStageAddress);
        Address thirdStageAddress = new Address(dataBank, secondStageOffset);
        Address address = Address.sumOffsetToAddressNoWrapAround(thirdStageAddress, indexWithYRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case DirectPageIndirectLongIndexedWithY:
      {
        Address firstStageAddress = new Address((directPage + readByte(mProgramAddress.newWithOffset1())));
        Address secondStageAddress = readLongAddressAt(firstStageAddress);
        Address address = Address.sumOffsetToAddressNoWrapAround(secondStageAddress, indexWithYRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case StackRelative:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = toShort(getStackPointer() + readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case StackDirectPageIndirect:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = toShort(directPage + readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case StackRelativeIndirectIndexedWithY:
      {
        Address firstStageAddress = new Address((getStackPointer() + readByte(mProgramAddress.newWithOffset1())));
        int secondStageOffset = readTwoBytes(firstStageAddress);
        Address thirdStageAddress = new Address(dataBank, secondStageOffset);
        Address address = Address.sumOffsetToAddressNoWrapAround(thirdStageAddress, indexWithYRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      default:
        throw new IllegalStateException("Unexpected value: " + addressingMode);
    }

    return new Address(dataAddressBank, dataAddressOffset);
  }

  public int readByte(Address address)
  {
    return 0;
  }

  public int readTwoBytes(Address address)
  {
    return 0;
  }

  public Address readLongAddressAt(Address address)
  {
    return new Address(0);
  }

  public int getD()
  {
    return directPage;
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
    mStackAddress = new Address(0x01FF);
  }

  public void clearStack(Address address)
  {
    mStackAddress = address;
  }

  public void push8Bit(int value)
  {
    storeByte(mStackAddress, value);
    mStackAddress.decrementOffsetBy(Sizeof.sizeofByte);
  }

  public void push16Bit(int value)
  {
    int leastSignificant = toByte((value) & 0xFF);
    int mostSignificant = toByte(((value) & 0xFF00) >> 8);
    push8Bit(mostSignificant);
    push8Bit(leastSignificant);
  }

  public int pull8Bit()
  {
    mStackAddress.incrementOffsetBy(Sizeof.sizeofByte);
    return readByte(mStackAddress);
  }

  public int pull16Bit()
  {
    return toShort((pull8Bit() | ((pull8Bit()) << 8)));
  }

  public int getStackPointer()
  {
    return mStackAddress.getOffset();
  }

  public void incA()
  {
    accumulator++;
    accumulator = toShort(accumulator);
  }

  public void decA()
  {
    accumulator--;
    accumulator = toShort(accumulator);
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
    this.address = address;
    this.read = true;
    this.validProgramAddress = true;
    this.validDataAddress = false;
  }

  public void readData(Address address)
  {
    this.address = address;
    this.read = true;
    this.validProgramAddress = false;
    this.validDataAddress = true;
  }

  public void readOpCode(Address address)
  {
    this.address = address;
    this.read = true;
    this.validProgramAddress = true;
    this.validDataAddress = true;
  }

  public void writeData(Address address)
  {
    this.address = address;
    this.read = false;
    this.validProgramAddress = false;
    this.validDataAddress = true;
  }

  public void noAddress()
  {
    this.address = mProgramAddress;
    this.read = true;
    this.validProgramAddress = false;
    this.validDataAddress = false;
  }

  public int getData()
  {
    return data;
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
    return address;
  }

  public void setRead(boolean read)
  {
    this.read = read;
  }

  public void incrementProgramAddress()
  {
    int offset = this.mProgramAddress.getOffset();
    offset++;
    this.mProgramAddress.setOffset(toShort(offset));
    if (offset > 0xffff)
    {
      int bank = this.mProgramAddress.getBank();
      bank++;
      this.mProgramAddress.setBank(toByte(bank));
    }
  }
}

