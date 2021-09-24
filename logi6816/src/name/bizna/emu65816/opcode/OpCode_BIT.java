package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_BIT
    extends OpCode
{
  public OpCode_BIT(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      execute8BitBIT(cpu);
    }
    else
    {
      execute16BitBIT(cpu);
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case BIT_Immediate:                 // BIT Immediate
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case BIT_Absolute:                 // BIT Absolute
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case BIT_DirectPage:                 // BIT Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case BIT_AbsoluteIndexedWithX:                 // BIT Absolute Indexed, X
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case BIT_DirectPageIndexedWithX:                 // BIT Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }

  protected void execute8BitBIT(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.readByte(addressOfOpCodeData);
    boolean isHighestBitSet = is8bitValueNegative(value);
    boolean isNextToHighestBitSet = (value & 0x40) != 0;

    if (getAddressingMode() != AddressingMode.Immediate)
    {
      cpu.getCpuStatus().setSignFlag(isHighestBitSet);
      cpu.getCpuStatus().setOverflowFlag(isNextToHighestBitSet);
    }
    cpu.getCpuStatus().updateZeroFlagFrom8BitValue((value & Binary.lower8BitsOf(cpu.getA())));
  }

  protected void execute16BitBIT(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.readTwoBytes(addressOfOpCodeData);
    boolean isHighestBitSet = (value & 0x8000) != 0;
    boolean isNextToHighestBitSet = (value & 0x4000) != 0;

    if (getAddressingMode() != AddressingMode.Immediate)
    {
      cpu.getCpuStatus().setSignFlag(isHighestBitSet);
      cpu.getCpuStatus().setOverflowFlag(isNextToHighestBitSet);
    }
    cpu.getCpuStatus().updateZeroFlagFrom16BitValue((value & cpu.getA()));
  }
}

