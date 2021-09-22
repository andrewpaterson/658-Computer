package name.bizna.emu65816;

import name.bizna.emu65816.opcode.OpCode;

import static name.bizna.emu65816.Address.sumOffsetToAddress;

public class Cpu65816
{
  protected SystemBus mSystemBus;
  protected EmulationModeInterrupts mEmulationInterrupts;
  protected NativeModeInterrupts mNativeInterrupts;

  // Accumulator register
  protected short mA = 0;
  // X index register
  protected short mX = 0;
  // Y index register
  protected short mY = 0;
  // Status register
  protected CpuStatus mCpuStatus;
  // Data bank register
  protected byte mDB = 0;
  // Direct page register
  protected short mD = 0;

  protected Pins mPins;

  protected Stack mStack;

  // Address of the current OpCode
  protected Address mProgramAddress;

  // Total number of cycles
  long mTotalCyclesCounter = 0;

  // OpCode Table.
  static OpCode[] OP_CODE_TABLE;

  public Cpu65816(SystemBus systemBus, EmulationModeInterrupts emulationInterrupts, NativeModeInterrupts nativeInterrupts)
  {
    mSystemBus = systemBus;
    mEmulationInterrupts = emulationInterrupts;
    mNativeInterrupts = nativeInterrupts;
    mProgramAddress = new Address((short) 0x0000);
    mStack = new Stack(mSystemBus);
    OP_CODE_TABLE = OpCodeTable.createTable();
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

  public void setXL(byte x)
  {
    mX = x;
  }

  public void setYL(byte y)
  {
    mY = y;
  }

  public void setX(short x)
  {
    mX = x;
  }

  public void setY(short y)
  {
    mY = y;
  }

  public void setA(short a)
  {
    mA = a;
  }

  public short getA()
  {
    return mA;
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
    mCpuStatus.setEmulationFlag();
    mCpuStatus.setAccumulatorWidthFlag();
    mCpuStatus.setIndexWidthFlag();
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

  public boolean executeNextInstruction()
  {
    if (mPins.RES)
    {
      return false;
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
        mCpuStatus.setInterruptDisableFlag();
        mProgramAddress = new Address(mSystemBus.readTwoBytes(new Address(mNativeInterrupts.interruptRequest)));
      }
      else
      {
        mStack.push16Bit(mProgramAddress.getOffset());
        mStack.push8Bit(mCpuStatus.getRegisterValue());
        mCpuStatus.setInterruptDisableFlag();
        mProgramAddress = new Address(mSystemBus.readTwoBytes(new Address(mEmulationInterrupts.brkIrq)));
      }
    }

    // Fetch the instruction
    byte instruction = mSystemBus.readByte(mProgramAddress);
    OpCode opCode = OP_CODE_TABLE[instruction];
    // Execute it
    opCode.execute(this);
    return true;
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

  public void subtractFromCycles(int cycles)
  {
    mTotalCyclesCounter -= cycles;
  }

  public void addToProgramAddress(int bytes)
  {
    mProgramAddress.incrementOffsetBy((short) bytes);
  }

  public void addToProgramAddressAndCycles(int bytes, int cycles)
  {
    addToCycles(cycles);
    addToProgramAddress(bytes);
  }

  public short indexWithXRegister()
  {
    return indexIs8BitWide() ? Binary.lower8BitsOf(mX) : mX;
  }

  public short indexWithYRegister()
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
        short firstStageOffset = (short) (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
        Address firstStageAddress = new Address(firstStageOffset);
        short secondStageOffset = mSystemBus.readTwoBytes(firstStageAddress);
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
    byte dataAddressBank;
    short dataAddressOffset;

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
        Address address = mSystemBus.readAddressAt(mProgramAddress.newWithOffset1());
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
        Address address = mSystemBus.readAddressAt(addressOfEffectiveAddress);
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
        Address firstStageAddress = mSystemBus.readAddressAt(mProgramAddress.newWithOffset1());
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
          // 6502 uses zero page
          dataAddressOffset = mSystemBus.readByte(mProgramAddress.newWithOffset1());
        }
        else
        {
          // 65816 uses direct page
          dataAddressOffset = (short) (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
        }
      }
      break;
      case DirectPageIndexedWithX:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = (short) (mD + indexWithXRegister() + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case DirectPageIndexedWithY:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = (short) (mD + indexWithYRegister() + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case DirectPageIndirect:
      {
        Address firstStageAddress = new Address((short) (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        dataAddressBank = mDB;
        dataAddressOffset = mSystemBus.readTwoBytes(firstStageAddress);
      }
      break;
      case DirectPageIndirectLong:
      {
        Address firstStageAddress = new Address((short) (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        Address address = mSystemBus.readAddressAt(firstStageAddress);
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case DirectPageIndexedIndirectWithX:
      {
        Address firstStageAddress = new Address((short) (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1()) + indexWithXRegister()));
        dataAddressBank = mDB;
        dataAddressOffset = mSystemBus.readTwoBytes(firstStageAddress);
      }
      break;
      case DirectPageIndirectIndexedWithY:
      {
        Address firstStageAddress = new Address((short) (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        short secondStageOffset = mSystemBus.readTwoBytes(firstStageAddress);
        Address thirdStageAddress = new Address(mDB, secondStageOffset);
        Address address = Address.sumOffsetToAddressNoWrapAround(thirdStageAddress, indexWithYRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case DirectPageIndirectLongIndexedWithY:
      {
        Address firstStageAddress = new Address((short) (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        Address secondStageAddress = mSystemBus.readAddressAt(firstStageAddress);
        Address address = Address.sumOffsetToAddressNoWrapAround(secondStageAddress, indexWithYRegister());
        dataAddressBank = address.getBank();
        dataAddressOffset = address.getOffset();
      }
      break;
      case StackRelative:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = (short) (mStack.getStackPointer() + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case StackDirectPageIndirect:
      {
        dataAddressBank = 0x00;
        dataAddressOffset = (short) (mD + mSystemBus.readByte(mProgramAddress.newWithOffset1()));
      }
      break;
      case StackRelativeIndirectIndexedWithY:
      {
        Address firstStageAddress = new Address((short) (mStack.getStackPointer() + mSystemBus.readByte(mProgramAddress.newWithOffset1())));
        short secondStageOffset = mSystemBus.readTwoBytes(firstStageAddress);
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

  public byte readByte(Address address)
  {
    return mSystemBus.readByte(address);
  }

  public short readTwoBytes(Address address)
  {
    return mSystemBus.readTwoBytes(address);
  }

  public short getD()
  {
    return mD;
  }
}

