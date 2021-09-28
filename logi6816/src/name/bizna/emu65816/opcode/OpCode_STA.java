package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_STA
    extends OpCode
{
  public OpCode_STA(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    if (cpu.isAccumulator8Bit())
    {
      cpu.storeByte(dataAddress, Binary.lower8BitsOf(cpu.getA()));
    }
    else
    {
      cpu.storeTwoBytes(dataAddress, cpu.getA());
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case STA_Absolute:  // STA Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case STA_AbsoluteLong:  // STA Absolute Long
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case STA_DirectPage:  // STA Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case STA_DirectPageIndirect:  // STA Direct Page Indirect
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case STA_DirectPageIndirectLong:  // STA Direct Page Indirect Long
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case STA_AbsoluteIndexedWithX:  // STA Absolute Indexed, X
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(5);
        break;
      }
      case STA_AbsoluteLongIndexedWithX:  // STA Absolute Long Indexed, X
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case STA_AbsoluteIndexedWithY:  // STA Absolute Indexed, Y
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(5);
        break;
      }
      case STA_DirectPageIndexedWithX:  // STA Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case STA_DirectPageIndexedIndirectWithX:  // STA Direct Page Indexed Indirect, X
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case STA_DirectPageIndirectIndexedWithY:  // STA Direct Page Indirect Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case STA_DirectPageIndirectLongIndexedWithY:  // STA Direct Page Indirect Long Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case STA_StackRelative:  // STA Stack Relative
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case STA_StackRelativeIndirectIndexedWithY:  // STA Stack Relative Indirect Indexed, Y
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(7);
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

