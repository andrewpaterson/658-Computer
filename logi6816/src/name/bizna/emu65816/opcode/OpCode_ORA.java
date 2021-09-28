package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_ORA
    extends OpCode
{
  public OpCode_ORA(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  public void executeORA8Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.get8BitData();
    int result = toByte(Binary.lower8BitsOf(cpu.getA()) | operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
  }

  public void executeORA16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.get16BitData();
    int result = (cpu.getA() | operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    cpu.setA(result);
  }

  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isAccumulator8Bit())
    {
      executeORA8Bit(cpu);
    }
    else
    {
      executeORA16Bit(cpu);
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case (0x09):                // ORA Immediate
      {
        if (cpu.isAccumulator16Bit())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case (0x0D):                // ORA Absolute
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case (0x0F):                // ORA Absolute Long
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case (0x05):                 // ORA Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case (0x12):                 // ORA Direct Page Indirect
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case (0x07):                 // ORA Direct Page Indirect Long
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case (0x1D):                 // ORA Absolute Indexed, X
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case (0x1F):                 // ORA Absolute Long Indexed, X
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case (0x19):                 // ORA Absolute Indexed, Y
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case (0x15):                 // ORA Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case (0x01):                // ORA Direct Page Indexed Indirect, X
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case (0x11):                 // ORA Direct Page Indirect Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case (0x17):                 // ORA Direct Page Indirect Long Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case (0x03):                // ORA Stack Relative
      {
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case (0x13):                 // ORA Stack Relative Indirect Indexed, Y
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

