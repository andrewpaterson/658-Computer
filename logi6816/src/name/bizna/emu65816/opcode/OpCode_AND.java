package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeName.*;

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
    int operand = cpu.get8BitData();
    int result = (Binary.getLowByte(cpu.getA()) & operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
  }

  protected void executeAND16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.get16BitData();
    int result = (cpu.getA() & operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    cpu.setA(result);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isMemory16Bit())
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
        if (cpu.isMemory16Bit())
        {
          cpu.addToProgramAddressAndCycles(3, 2);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 2);
        }
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
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToProgramAddressAndCycles(2, 4);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 3);
        }
        break;
      }
      case AND_DirectPageIndirect:                // AND Direct Page Indirect
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToProgramAddressAndCycles(2, 6);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 5);
        }
        break;
      }
      case AND_DirectPageIndirectLong:                // AND Direct Page Indirect Long
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToProgramAddressAndCycles(2, 7);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 6);
        }
        break;
      }
      case AND_AbsoluteIndexedWithX:                // AND Absolute Indexed, X
      {
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
          cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case AND_DirectPageIndexedWithX:                // AND Direct Page Indexed, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToProgramAddressAndCycles(2, 5);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 4);
        }
        break;
      }
      case AND_DirectPageIndexedIndirectWithX:                // AND Direct Page Indexed Indirect, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToProgramAddressAndCycles(2, 7);
        }
        else
        {
          cpu.addToProgramAddressAndCycles(2, 6);
        }
        break;
      }
      case AND_DirectPageIndirectIndexedWithY:                // AND Direct Page Indirect Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case AND_DirectPageIndirectLongIndexedWithY:                // AND Direct Page Indirect Long Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        //Add 1 cycle if branch is taken across page boundary in 6502 emulation mode
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

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

