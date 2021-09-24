package name.bizna.emu65816;

import name.bizna.emu65816.opcode.OpCode;

import static name.bizna.emu65816.Address.sumOffsetToAddress;
import static name.bizna.emu65816.Unsigned.toShort;

public class Cpu65816
{
  protected SystemBus mSystemBus;
  protected EmulationModeInterrupts mEmulationInterrupts;
  protected NativeModeInterrupts mNativeInterrupts;

  // Status register
  protected CpuStatus mCpuStatus;
  protected Pins mPins;
  protected Stack mStack;
  protected boolean mStopped;

  // Accumulator register (short)
  protected int mA;
  // X index register (short)
  protected int mX;
  // Y index register (short)
  protected int mY;
  // Data bank register (short)
  protected int mDB;
  // Direct page register (short)
  protected int mD;
  // Address of the current OpCode
  protected Address mProgramAddress;

  // Total number of cycles
  long mTotalCyclesCounter;

  // OpCode Table.
  protected static OpCode[] OP_CODE_TABLE;

  public Cpu65816(SystemBus systemBus, EmulationModeInterrupts emulationInterrupts, NativeModeInterrupts nativeInterrupts)
  {
    mSystemBus = systemBus;
    mEmulationInterrupts = emulationInterrupts;
    mNativeInterrupts = nativeInterrupts;
    mProgramAddress = new Address(0x0000);
    mStack = new Stack(mSystemBus);
    OP_CODE_TABLE = OpCodeTable.createTable();
    mPins = new Pins();
    mTotalCyclesCounter = 0;
    mA = 0;
    mX = 0;
    mY = 0;
    mDB = 0;
    mD = 0;
    mCpuStatus = new CpuStatus();
    mStopped = false;
  }

  public void setRESPin(boolean value)
  {
    if (!value && mPins.RES)
    {
      reset();
    }
    mPins.RES = value;
  }

  public void setIRQPin(boolean value)
  {
    mPins.IRQ = value;
  }

  public void setNMIPin(boolean value)
  {
    mPins.NMI = value;
  }

  public void setABORTPin(boolean value)
  {
    mPins.ABORT = value;
  }

  public void setX(int x)
  {
    mX = x;
  }

  public void setY(int y)
  {
    mY = y;
  }

  public void setA(int a)
  {
    mA = a;
  }

  public int getA()
  {
    return mA;
  }

  public int getX()
  {
    return mX;
  }

  public int getY()
  {
    return mY;
  }

  public int getDB()
  {
    return mDB;
  }

  public void setDB(int mDB)
  {
    this.mDB = toShort(mDB);
  }

  public void setD(int mD)
  {
    this.mD = toShort(mD);
  }

  public Address getProgramAddress()
  {
    return mProgramAddress;
  }

  public Stack getStack()
  {
    return mStack;
  }

  public CpuStatus getCpuStatus()
  {
    return mCpuStatus;
  }

  /**
   * Resets the cpu to its initial state.
   */
  public void reset()
  {
    setRESPin(true);
    mCpuStatus.setEmulationFlag(true);
    mCpuStatus.setAccumulatorWidthFlag(true);
    mCpuStatus.setIndexWidthFlag(true);
    mX &= 0xFF;
    mY &= 0xFF;
    mD = 0x0;
    mStack = new Stack(mSystemBus);
    mProgramAddress = new Address(mEmulationInterrupts.reset);
    mProgramAddress = new Address(mSystemBus.readTwoBytes(mProgramAddress));
  }

  public void setRDYPin(boolean value)
  {
    mPins.RDY = value;
  }

  public void executeNextInstruction()
  {
    if ((mPins.RES) || (mStopped))
    {
      return;
    }

    if ((mPins.IRQ) && (!mCpuStatus.interruptDisableFlag()))
    {
        /*
            The program bank register (PB, the A16-A23 part of the address bus) is pushed onto the hardware stack (65C816/65C802 only when operating in native mode).
            The most significant byte (MSB) of the program counter (PC) is pushed onto the stack.
            The least significant byte (LSB) of the program counter is pushed onto the stack.
            The status register (SR) is pushed onto the stack.
            The interrupt disable flag is set in the status register.
            PB is loaded with $00 (65C816/65C802 only when operating in native mode).
            PC is loaded from the relevant vector (see tables).
        */
      if (!mCpuStatus.emulationFlag())
      {
        mStack.push8Bit(mProgramAddress.getBank());
        mStack.push16Bit(mProgramAddress.getOffset());
        mStack.push8Bit(mCpuStatus.getRegisterValue());
        mCpuStatus.setInterruptDisableFlag(true);
        mProgramAddress = new Address(mSystemBus.readTwoBytes(new Address(mNativeInterrupts.interruptRequest)));
      }
      else
      {
        mStack.push16Bit(mProgramAddress.getOffset());
        mStack.push8Bit(mCpuStatus.getRegisterValue());
        mCpuStatus.setInterruptDisableFlag(true);
        mProgramAddress = new Address(mSystemBus.readTwoBytes(new Address(mEmulationInterrupts.brkIrq)));
      }
    }

    // Fetch the instruction
    int instruction = mSystemBus.readByte(mProgramAddress);
    OpCode opCode = OP_CODE_TABLE[instruction];
    System.out.println(opCode.getName());

    // Execute it
    opCode.execute(this);
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
    return (indexIs8BitWide() ? Binary.lower8BitsOf(mX) : mX);
  }

  public int indexWithYRegister()
  {
    return indexIs8BitWide() ? Binary.lower8BitsOf(mY) : mY;
  }

  public void setProgramAddress(Address address)
  {
    mProgramAddress = address;
  }

  public boolean opCodeAddressingCrossesPageBoundary(AddressingMode addressingMode)
  {
    switch (addressingMode)
    {
      case AbsoluteIndexedWithX:
      {
        Address initialAddress = new Address(mDB, mSystemBus.readTwoBytes(mProgramAddress.newWithOffset1()));
        // TODO: figure out when to wrap around and when not to, it should not matter in this case
        // but it matters when fetching data
        Address finalAddress = sumOffsetToAddress(initialAddress, indexWithXRegister());
        return Address.offsetsAreOnDifferentPages(initialAddress.getOffset(), finalAddress.getOffset());
      }
      case AbsoluteIndexedWithY:
      {
        Address initialAddress = new Address(mDB, mSystemBus.readTwoBytes(mProgramAddress.newWithOffset1()));
        // TODO: figure out when to wrap around and when not to, it should not matter in this case
        // but it matters when fetching data
        Address finalAddress = sumOffsetToAddress(initialAddress, indexWithYRegister());
        return Address.offsetsAreOnDifferentPages(initialAddress.getOffset(), finalAddress.getOffset());
      }
      case DirectPageIndirectIndexedWithY:
      {
        int firstStageOffset = toShort(mD + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
        Address firstStageAddress = new Address(firstStageOffset);
        int secondStageOffset = mSystemBus.readTwoBytes(firstStageAddress);
        Address thirdStageAddress = new Address(mDB, secondStageOffset);
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
        Address address = mProgramAddress.newWithOffset1();
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;

      case Absolute:
      {
        dataAddressBank = mDB;
        dataAddressOffset = mSystemBus.readTwoBytes(mProgramAddress.newWithOffset1());
      }
      break;
      case AbsoluteLong:
      {
        Address address = mSystemBus.readLongAddressAt(mProgramAddress.newWithOffset1());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;

      case AbsoluteIndirect:
      {
        dataAddressBank = mProgramAddress.getBank();
        Address addressOfOffset = new Address(mSystemBus.readTwoBytes(mProgramAddress.newWithOffset1()));
        dataAddressOffset = mSystemBus.readTwoBytes(addressOfOffset);
      }
      break;
      case AbsoluteIndirectLong:
      {
        Address addressOfEffectiveAddress = new Address(mSystemBus.readTwoBytes(mProgramAddress.newWithOffset1()));
        Address address = mSystemBus.readLongAddressAt(addressOfEffectiveAddress);
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case AbsoluteIndexedIndirectWithX:
      {
        Address firstStageAddress = new Address(mProgramAddress.getBank(), mSystemBus.readTwoBytes(mProgramAddress.newWithOffset1()));

        Address secondStageAddress = firstStageAddress.newWithOffsetNoWrapAround(indexWithXRegister());
        dataAddressBank = mProgramAddress.getBank();
        dataAddressOffset = mSystemBus.readTwoBytes(secondStageAddress);
      }
      break;
      case AbsoluteIndexedWithX:
      {
        Address firstStageAddress = new Address(mDB, mSystemBus.readTwoBytes(mProgramAddress.newWithOffset1()));
        Address address = Address.sumOffsetToAddressNoWrapAround(firstStageAddress, indexWithXRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case AbsoluteLongIndexedWithX:
      {
        Address firstStageAddress = mSystemBus.readLongAddressAt(mProgramAddress.newWithOffset1());
        Address address = Address.sumOffsetToAddressNoWrapAround(firstStageAddress, indexWithXRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case AbsoluteIndexedWithY:
      {
        Address firstStageAddress = new Address(mDB, mSystemBus.readTwoBytes(mProgramAddress.newWithOffset1()));
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
          dataAddressOffset = mSystemBus.readByte(mProgramAddress.newWithOffset1());
        }
        else
        {
          dataAddressOffset = toShort(mD + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
        }
      }
      break;
      case DirectPageIndexedWithX:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = toShort(mD + indexWithXRegister() + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case DirectPageIndexedWithY:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = toShort(mD + indexWithYRegister() + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case DirectPageIndirect:
      {
        Address firstStageAddress = new Address((mD + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        dataAddressBank = mDB;
        dataAddressOffset = mSystemBus.readTwoBytes(firstStageAddress);
      }
      break;
      case DirectPageIndirectLong:
      {
        Address firstStageAddress = new Address( (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        Address address = mSystemBus.readLongAddressAt(firstStageAddress);
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case DirectPageIndexedIndirectWithX:
      {
        Address firstStageAddress = new Address( (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1()) + indexWithXRegister()));
        dataAddressBank = mDB;
        dataAddressOffset = mSystemBus.readTwoBytes(firstStageAddress);
      }
      break;
      case DirectPageIndirectIndexedWithY:
      {
        Address firstStageAddress = new Address( (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        int secondStageOffset = mSystemBus.readTwoBytes(firstStageAddress);
        Address thirdStageAddress = new Address(mDB, secondStageOffset);
        Address address = Address.sumOffsetToAddressNoWrapAround(thirdStageAddress, indexWithYRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case DirectPageIndirectLongIndexedWithY:
      {
        Address firstStageAddress = new Address((mD + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        Address secondStageAddress = mSystemBus.readLongAddressAt(firstStageAddress);
        Address address = Address.sumOffsetToAddressNoWrapAround(secondStageAddress, indexWithYRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case StackRelative:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = toShort (mStack.getStackPointer() + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case StackDirectPageIndirect:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = toShort (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case StackRelativeIndirectIndexedWithY:
      {
        Address firstStageAddress = new Address( (mStack.getStackPointer() + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        int secondStageOffset = mSystemBus.readTwoBytes(firstStageAddress);
        Address thirdStageAddress = new Address(mDB, secondStageOffset);
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
    return mSystemBus.readByte(address);
  }

  public int readTwoBytes(Address address)
  {
    return mSystemBus.readTwoBytes(address);
  }

  public int getD()
  {
    return mD;
  }

  public void storeTwoBytes(Address address, int value)
  {
    mSystemBus.storeTwoBytes(address, value);
  }

  public void storeByte(Address address, int value)
  {
    mSystemBus.storeByte(address, value);
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
    mStack = new Stack(mSystemBus);
  }

  public void clearStack(Address address)
  {
    mStack = new Stack(mSystemBus, address);
  }

  public void incA()
  {
    mA++;
    mA = toShort(mA);
  }

  public void decA()
  {
    mA--;
    mA = toShort(mA);
  }

  public void incY()
  {
    mY++;
    mY = toShort(mY);
  }

  public void incX()
  {
    mX++;
    mX = toShort(mX);
  }

  public void decY()
  {
    mY--;
    mY = toShort(mY);
  }

  public void decX()
  {
    mX--;
    mX = toShort(mX);
  }

  public void stop()
  {
    mStopped = true;
  }

  public boolean isStopped()
  {
    return mStopped;
  }
}

