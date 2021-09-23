package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_AND
    extends OpCode
{
  public OpCode_AND(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void executeAND8Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte operand = cpu.readByte(opCodeDataAddress);
    byte result = (byte) (Binary.lower8BitsOf(cpu.getA()) & operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
  }

  protected void executeAND16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    short operand = cpu.readTwoBytes(opCodeDataAddress);
    short result = (short) (cpu.getA() & operand);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    cpu.setA(result);
  }

  @Override
  public void execute(Cpu65816 cpu)
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
      case (0x29):                // AND Immediate
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case (0x2D):                // AND Absolute
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case (0x2F):                // AND Absolute Long
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case (0x25):                // AND Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case (0x32):                // AND Direct Page Indirect
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case (0x27):                // AND Direct Page Indirect Long
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case (0x3D):                // AND Absolute Indexed, X
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case (0x3F):                // AND Absolute Long Indexed, X
      {
        cpu.addToProgramAddressAndCycles(4, 5);
        break;
      }
      case (0x39):                // AND Absolute Indexed, Y
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case (0x35):                // AND Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case (0x21):                // AND Direct Page Indexed Indirect, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      case (0x31):                // AND Direct Page Indirect Indexed, Y
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
      case (0x37):                // AND Direct Page Indirect Long Indexed, Y
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
      case (0x23):                // AND Stack Relative
      {
        cpu.addToProgramAddressAndCycles(2, 4);
        break;
      }
      case (0x33):                // AND Stack Relative Indirect Indexed, Y
      {
        cpu.addToProgramAddressAndCycles(2, 7);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}
