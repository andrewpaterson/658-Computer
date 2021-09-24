package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_AND
    extends OpCode
{
  public OpCode_AND(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void executeAND8Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.readByte(opCodeDataAddress);
    int result = (Binary.lower8BitsOf(cpu.getA()) & operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
  }

  protected void executeAND16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.readTwoBytes(opCodeDataAddress);
    int result = (cpu.getA() & operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    cpu.setA(result);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    if (cpu.accumulatorIs16BitWide())
    {
      executeAND16Bit(cpu);
      cpu.addToCycles(1);
    }
    else
    {
      executeAND8Bit(cpu);
    }

    switch (getCode())
    {
      case AND_Immediate:                // AND Immediate
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case AND_Absolute:                // AND Absolute
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case AND_AbsoluteLong:                // AND Absolute Long
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case AND_DirectPage:                // AND Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case AND_DirectPageIndirect:                // AND Direct Page Indirect
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case AND_DirectPageIndirectLong:                // AND Direct Page Indirect Long
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case AND_AbsoluteIndexedWithX:                // AND Absolute Indexed, X
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case AND_AbsoluteLongIndexedWithX:                // AND Absolute Long Indexed, X
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case AND_AbsoluteIndexedWithY:                // AND Absolute Indexed, Y
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case AND_DirectPageIndexedWithX:                // AND Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case AND_DirectPageIndexedIndirectWithX:                // AND Direct Page Indexed Indirect, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case AND_DirectPageIndirectIndexedWithY:                // AND Direct Page Indirect Indexed, Y
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
      case AND_DirectPageIndirectLongIndexedWithY:                // AND Direct Page Indirect Long Indexed, Y
      {
        // TODO: The manual reports a '0' not on the cycles count for this OpCode.
        // No idea what that means.
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case AND_StackRelative:                // AND Stack Relative
      {
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case AND_StackRelativeIndirectIndexedWithY:                // AND Stack Relative Indirect Indexed, Y
      {
        cpu.addToProgramAddressAndCycles(2, 7);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

