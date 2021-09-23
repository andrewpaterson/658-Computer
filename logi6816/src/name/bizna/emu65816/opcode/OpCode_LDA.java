package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_LDA
    extends OpCode
{
  public OpCode_LDA(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void executeLDA8Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.readByte(opCodeDataAddress);
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), value));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  protected void executeLDA16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    cpu.setA(cpu.readTwoBytes(opCodeDataAddress));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs16BitWide())
    {
      executeLDA16Bit(cpu);
      cpu.addToCycles(1);
    }
    else
    {
      executeLDA8Bit(cpu);
    }

    switch (getCode())
    {
      case LDA_Immediate:                // LDA Immediate
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case LDA_Absolute:                // LDA Absolute
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case LDA_AbsoluteLong:                // LDA Absolute Long
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case LDA_DirectPage:                // LDA Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case LDA_DirectPageIndirect:                // LDA Direct Page Indirect
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case LDA_DirectPageIndirectLong:                // LDA Direct Page Indirect Long
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case LDA_AbsoluteIndexedWithX:                // LDA Absolute Indexed, X
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case LDA_AbsoluteLongIndexedWithX:                // LDA Absolute Long Indexed, X
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case LDA_AbsoluteIndexedWithY:                // LDA Absolute Indexed, Y
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case LDA_DirectPageIndexedWithX:                // LDA Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case LDA_DirectPageIndexedIndirectWithX:                // LDA Direct Page Indexed Indirect, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case LDA_DirectPageIndirectIndexedWithY:                // LDA Direct Page Indirect Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case LDA_DirectPageIndirectLongIndexedWithY:                // LDA Direct Page DP Indirect Long Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case LDA_StackRelative:                // LDA Stack Relative
      {
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case LDA_StackRelativeIndirectIndexedWithY:                // LDA Stack Relative Indirect Indexed, Y
      {
        cpu.addToProgramAddressAndCycles(2, 7);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

