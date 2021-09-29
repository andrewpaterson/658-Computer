package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_EOR
    extends OpCode
{
  public OpCode_EOR(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void executeEOR8Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.get8BitData();
    int result = toByte(Binary.getLowByte(cpu.getA()) ^ operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
  }

  protected void executeEOR16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.get16BitData();
    int result = toShort(cpu.getA() ^ operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    cpu.setA(result);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      executeEOR8Bit(cpu);
    }
    else
    {
      executeEOR16Bit(cpu);
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case EOR_Immediate:                // EOR Immediate
      {
        if (cpu.isMemory16Bit())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case EOR_Absolute:                // EOR Absolute
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case EOR_AbsoluteLong:                // EOR Absolute Long
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case EOR_DirectPage:                 // EOR Direct Page
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case EOR_DirectPageIndirect:                 // EOR Direct Page Indirect
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case EOR_DirectPageIndirectLong:                 // EOR Direct Page Indirect Long
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case EOR_AbsoluteIndexedWithX:                 // EOR Absolute Indexed, X
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case EOR_AbsoluteLongIndexedWithX:                 // EOR Absolute Long Indexed, X
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case EOR_AbsoluteIndexedWithY:                 // EOR Absolute Indexed, Y
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case EOR_DirectPageIndexedWithX:                 // EOR Direct Page Indexed, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case EOR_DirectPageIndexedIndirectWithX:                // EOR Direct Page Indexed Indirect, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case EOR_DirectPageIndirectIndexedWithY:                 // EOR Direct Page Indirect Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case EOR_DirectPageIndirectLongIndexedWithY:                 // EOR Direct Page Indirect Long Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case EOR_StackRelative:                // EOR Stack Relative
      {
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case EOR_StackRelativeIndirectIndexedWithY:                // EOR Stack Relative Indirect Indexed, Y
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

