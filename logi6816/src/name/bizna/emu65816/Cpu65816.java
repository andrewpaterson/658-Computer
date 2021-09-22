package name.bizna.emu65816;

public class Cpu65816
{
  void setRESPin(boolean value)
  {
    if (!value && mPins.RES)
    {
      reset();
    }
    mPins.RES = value;
  }

  void setIRQPin(boolean value)
  {
    mPins.IRQ = value;
  }

  void setNMIPin(boolean value)
  {
    mPins.NMI = value;
  }

  void setABORTPin(boolean value)
  {
    mPins.ABORT = value;
  }

  private SystemBus mSystemBus;
  private EmulationModeInterrupts mEmulationInterrupts;
  private NativeModeInterrupts mNativeInterrupts;

  // Accumulator register
  private short mA = 0;
  // X index register
  private short mX = 0;
  // Y index register
  private short mY = 0;
  // Status register
  private CpuStatus mCpuStatus;
  // Data bank register
  private byte mDB = 0;
  // Direct page register
  private short mD = 0;

  private Pins mPins;

  private Stack mStack;

  // Address of the current OpCode
  private Address mProgramAddress;

  // Total number of cycles
  long mTotalCyclesCounter = 0;

  // OpCode Table.
  static OpCode[] OP_CODE_TABLE;

  // OpCodes handling routines.
  // Implementations for these methods can be found in the corresponding OpCode_XXX.cpp file.
//  void executeORA(OpCode &);
//  void executeORA8Bit(OpCode &);
//  void executeORA16Bit(OpCode &);
//  void executeStack(OpCode &);
//  void executeStatusReg(OpCode &);
//  void executeMemoryROL(OpCode &);
//  void executeAccumulatorROL(OpCode &);
//  void executeROL(OpCode &);
//  void executeMemoryROR(OpCode &);
//  void executeAccumulatorROR(OpCode &);
//  void executeROR(OpCode &);
//  void executeInterrupt(OpCode &);
//  void executeJumpReturn(OpCode &);
//  void execute8BitSBC(OpCode &);
//  void execute16BitSBC(OpCode &);
//  void execute8BitBCDSBC(OpCode &);
//  void execute16BitBCDSBC(OpCode &);
//  void executeSBC(OpCode &);
//  void execute8BitADC(OpCode &);
//  void execute16BitADC(OpCode &);
//  void execute8BitBCDADC(OpCode &);
//  void execute16BitBCDADC(OpCode &);
//  void executeADC(OpCode &);
//  void executeSTA(OpCode &);
//  void executeSTX(OpCode &);
//  void executeSTY(OpCode &);
//  void executeSTZ(OpCode &);
//  void executeTransfer(OpCode &);
//  void executeMemoryASL(OpCode &);
//  void executeAccumulatorASL(OpCode &);
//  void executeASL(OpCode &);
//  void executeAND8Bit(OpCode &);
//  void executeAND16Bit(OpCode &);
//  void executeAND(OpCode &);
//  void executeLDA8Bit(OpCode &);
//  void executeLDA16Bit(OpCode &);
//  void executeLDA(OpCode &);
//  void executeLDX8Bit(OpCode &);
//  void executeLDX16Bit(OpCode &);
//  void executeLDX(OpCode &);
//  void executeLDY8Bit(OpCode &);
//  void executeLDY16Bit(OpCode &);
//  void executeLDY(OpCode &);
//  void executeEOR8Bit(OpCode &);
//  void executeEOR16Bit(OpCode &);
//  void executeEOR(OpCode &);
//  int executeBranchShortOnCondition(boolean, OpCode &);
//  int executeBranchLongOnCondition(boolean, OpCode &);
//  void executeBranch(OpCode &);
//  void execute8BitCMP(OpCode &);
//  void execute16BitCMP(OpCode &);
//  void executeCMP(OpCode &);
//  void execute8BitDecInMemory(OpCode &);
//  void execute16BitDecInMemory(OpCode &);
//  void execute8BitIncInMemory(OpCode &);
//  void execute16BitIncInMemory(OpCode &);
//  void executeINCDEC(OpCode &);
//  void execute8BitCPX(OpCode &);
//  void execute16BitCPX(OpCode &);
//  void execute8BitCPY(OpCode &);
//  void execute16BitCPY(OpCode &);
//  void executeCPXCPY(OpCode &);
//  void execute8BitTSB(OpCode &);
//  void execute16BitTSB(OpCode &);
//  void execute8BitTRB(OpCode &);
//  void execute16BitTRB(OpCode &);
//  void executeTSBTRB(OpCode &);
//  void execute8BitBIT(OpCode &);
//  void execute16BitBIT(OpCode &);
//  void executeBIT(OpCode &);
//  void executeMemoryLSR(OpCode &);
//  void executeAccumulatorLSR(OpCode &);
//  void executeLSR(OpCode &);
//  void executeMisc(OpCode &);

  Cpu65816(SystemBus systemBus, EmulationModeInterrupts emulationInterrupts, NativeModeInterrupts nativeInterrupts)
  {
    mSystemBus = systemBus;
    mEmulationInterrupts = emulationInterrupts;
    mNativeInterrupts = nativeInterrupts;
    mProgramAddress = new Address((byte) 0x00, (short) 0x0000);
    mStack = new Stack(mSystemBus);
  }

  void setXL(byte x)
  {
    mX = x;
  }

  void setYL(byte y)
  {
    mY = y;
  }

  void setX(short x)
  {
    mX = x;
  }

  void setY(short y)
  {
    mY = y;
  }

  void setA(short a)
  {
    mA = a;
  }

  short getA()
  {
    return mA;
  }

  Address getProgramAddress()
  {
    return mProgramAddress;
  }

  Stack getStack()
  {
    return mStack;
  }

  CpuStatus getCpuStatus()
  {
    return mCpuStatus;
  }

  /**
   * Resets the cpu to its initial state.
   */
  void reset()
  {
    setRESPin(true);
    mCpuStatus.setEmulationFlag();
    mCpuStatus.setAccumulatorWidthFlag();
    mCpuStatus.setIndexWidthFlag();
    mX &= 0xFF;
    mY &= 0xFF;
    mD = 0x0;
    mStack = new Stack(mSystemBus);
    mProgramAddress = new Address((byte) 0x00, mEmulationInterrupts.reset);
    mProgramAddress = new Address((byte) 0x00, mSystemBus.readTwoBytes(mProgramAddress));
  }

  void setRDYPin(boolean value)
  {
    mPins.RDY = value;
  }

  boolean executeNextInstruction()
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
        mProgramAddress = new Address((byte) 0x00, mSystemBus.readTwoBytes(new Address((byte) 0x00, mNativeInterrupts.interruptRequest)));
      }
      else
      {
        mStack.push16Bit(mProgramAddress.getOffset());
        mStack.push8Bit(mCpuStatus.getRegisterValue());
        mCpuStatus.setInterruptDisableFlag();
        mProgramAddress = new Address((byte) 0x00, mSystemBus.readTwoBytes(new Address((byte) 0x00, mEmulationInterrupts.brkIrq)));
      }
    }

    // Fetch the instruction
    byte instruction = mSystemBus.readByte(mProgramAddress);
    OpCode opCode = OP_CODE_TABLE[instruction];
    // Execute it
    return opCode.execute(this);
  }

  boolean accumulatorIs8BitWide()
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

  boolean accumulatorIs16BitWide()
  {
    return !accumulatorIs8BitWide();
  }

  boolean indexIs8BitWide()
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

  boolean indexIs16BitWide()
  {
    return !indexIs8BitWide();
  }

  void addToCycles(int cycles)
  {
    mTotalCyclesCounter += cycles;
  }

  void subtractFromCycles(int cycles)
  {
    mTotalCyclesCounter -= cycles;
  }

  void addToProgramAddress(int bytes)
  {
    mProgramAddress.incrementOffsetBy((short) bytes);
  }

  void addToProgramAddressAndCycles(int bytes, int cycles)
  {
    addToCycles(cycles);
    addToProgramAddress(bytes);
  }

  short indexWithXRegister()
  {
    return indexIs8BitWide() ? Binary.lower8BitsOf(mX) : mX;
  }

  short indexWithYRegister()
  {
    return indexIs8BitWide() ? Binary.lower8BitsOf(mY) : mY;
  }

  void setProgramAddress(Address address)
  {
    mProgramAddress = address;
  }
}

