package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCodeInterrupt extends OpCode
{
  public OpCodeInterrupt(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeInterrupt(OpCode &opCode)
{
  switch (opCode.getCode())
  {
    case(0x00):  // BRK
    {
      if (mCpuStatus.emulationFlag())
      {
        mStack.push16Bit((uint16_t)(mProgramAddress.getOffset() + 2));
        mCpuStatus.setBreakFlag();
        mStack.push8Bit(mCpuStatus.getRegisterValue());
        mCpuStatus.setInterruptDisableFlag();

        setProgramAddress(Address(0x00, mEmulationInterrupts->brkIrq));
        addToCycles(7);
      }
      else
      {
        mStack.push8Bit(mProgramAddress.getBank());
        mStack.push16Bit((uint16_t)(mProgramAddress.getOffset() + 2));
        mStack.push8Bit(mCpuStatus.getRegisterValue());
        mCpuStatus.setInterruptDisableFlag();
        mCpuStatus.clearDecimalFlag();
        Address newAddress(0x00, mNativeInterrupts->brk);
        setProgramAddress(newAddress);
        addToCycles(8);
      }
      break;
    }
    case(0x02):                 // COP
    {
      if (mCpuStatus.emulationFlag())
      {
        mStack.push16Bit((uint16_t)(mProgramAddress.getOffset() + 2));
        mStack.push8Bit(mCpuStatus.getRegisterValue());
        mCpuStatus.setInterruptDisableFlag();
        setProgramAddress(Address(0x00, mEmulationInterrupts->coProcessorEnable));
        addToCycles(7);
      }
      else
      {
        mStack.push8Bit(mProgramAddress.getBank());
        mStack.push16Bit((uint16_t)(mProgramAddress.getOffset() + 2));
        mStack.push8Bit(mCpuStatus.getRegisterValue());
        mCpuStatus.setInterruptDisableFlag();
        setProgramAddress(Address(0x00, mNativeInterrupts->coProcessorEnable));
        addToCycles(8);
      }
      mCpuStatus.clearDecimalFlag();
      break;
    }
    case(0x40):                 // RTI
    {
      // Note: The picture in the 65816 programming manual about this looks wrong.
      // This implementation follows the text instead.
      mCpuStatus.setRegisterValue(mStack.pull8Bit());

      if (mCpuStatus.emulationFlag())
      {
        Address newProgramAddress(mProgramAddress.getBank(), mStack.pull16Bit());
        mProgramAddress = newProgramAddress;
        addToCycles(6);
      }
      else
      {
        uint16_t offset = mStack.pull16Bit();
        uint8_t bank = mStack.pull8Bit();
        Address newProgramAddress(bank, offset);
        mProgramAddress = newProgramAddress;
        addToCycles(7);
      }
      break;
    }
    default: {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
