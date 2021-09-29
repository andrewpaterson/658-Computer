package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_LDY
    extends OpCode
{
  public OpCode_LDY(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void executeLDY8Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get8BitData();
    cpu.setY(Binary.setLower8BitsOf16BitsValue(cpu.getY(), value));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  protected void executeLDY16Bit(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    cpu.setY(cpu.get16BitData());
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getY());
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex16Bit())
    {
      executeLDY16Bit(cpu);
      cpu.addToCycles(1);
    }
    else
    {
      executeLDY8Bit(cpu);
    }

    switch (getCode())
    {
      case LDY_Immediate:                // LDY Immediate
      {
        if (cpu.isIndex16Bit())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case LDY_Absolute:                // LDY Absolute
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case LDY_DirectPage:                // LDY Direct Page
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
        break;
      }
      case LDY_AbsoluteIndexedWithX:                // LDY Absolute Indexed, X
      {
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case LDY_DirectPageIndexedWithX:                // LDY Direct Page Indexed, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
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

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

