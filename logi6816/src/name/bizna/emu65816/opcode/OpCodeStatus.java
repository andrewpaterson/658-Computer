package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCodeStatus
    extends OpCode
{
  public OpCodeStatus(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

    switch (opCode.getCode())
  {
    case(0xC2):  // REP #const
    {
      uint8_t value = mSystemBus.readByte(getAddressOfOpCodeData(opCode));
      uint8_t statusByte = mCpuStatus.getRegisterValue();
      mCpuStatus.setRegisterValue(statusByte & ~value);
      addToProgramAddressAndCycles(2, 3);
      break;
    }
    case(0x38):  // SEC
    {
      mCpuStatus.setCarryFlag(true);
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0xF8):  // SED
    {
      mCpuStatus.setDecimalFlag();
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x78):  // SEI
    {
      mCpuStatus.setInterruptDisableFlag();
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x58):  // CLI
    {
      mCpuStatus.clearInterruptDisableFlag();
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0xE2):  // SEP
    {
      uint8_t value = mSystemBus.readByte(getAddressOfOpCodeData(opCode));
      if (mCpuStatus.emulationFlag()) {
        // In emulation mode status bits 4 and 5 are not affected
        // 0xCF = 11001111
        value &= 0xCF;
      }
      uint8_t statusReg = mCpuStatus.getRegisterValue();
      statusReg |= value;
      mCpuStatus.setRegisterValue(statusReg);

      addToProgramAddressAndCycles(2, 3);
      break;
    }
    case(0x18):  // CLC
    {
      mCpuStatus.setCarryFlag(false);
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0xD8):  // CLD
    {
      mCpuStatus.clearDecimalFlag();
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0xB8):  // CLV
    {
      mCpuStatus.clearOverflowFlag();
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0xFB):  // XCE
    {
      bool oldCarry = mCpuStatus.carryFlag();
      bool oldEmulation = mCpuStatus.emulationFlag();
      if (oldCarry) mCpuStatus.setEmulationFlag();
      else mCpuStatus.clearEmulationFlag();
      mCpuStatus.setCarryFlag(oldEmulation);

      mX &= 0xFF;
      mY &= 0xFF;

      if (mCpuStatus.emulationFlag())
      {
        mCpuStatus.setAccumulatorWidthFlag();
        mCpuStatus.setIndexWidthFlag();
      } else
      {
        mCpuStatus.clearAccumulatorWidthFlag();
        mCpuStatus.clearIndexWidthFlag();
      }

      // New stack
      mStack = Stack(&mSystemBus);

      addToProgramAddressAndCycles(1,2);
      break;
    }
    default: {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
