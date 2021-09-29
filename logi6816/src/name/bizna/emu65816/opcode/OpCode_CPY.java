package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_CPY
    extends OpCode
{
  public OpCode_CPY(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitCPY(Cpu65816 cpu)
  {
    int value = cpu.get8BitData(cpu.getAddressOfOpCodeData(getAddressingMode()));
    int result = toByte(Binary.getLowByte(cpu.getY()) - value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    boolean carry = Binary.getLowByte(cpu.getY()) >= value;
    cpu.getCpuStatus().setCarryFlag(carry);
  }

  protected void execute16BitCPY(Cpu65816 cpu)
  {
    int value = cpu.get16BitData(cpu.getAddressOfOpCodeData(getAddressingMode()));
    int result = toShort(cpu.getY() - value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    boolean carry = cpu.getY() >= value;
    cpu.getCpuStatus().setCarryFlag(carry);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case CPY_Immediate:  // CPY Immediate
      {
        if (cpu.isIndex8Bit())
        {
          execute8BitCPY(cpu);
        }
        else
        {
          execute16BitCPY(cpu);
          cpu.addToProgramAddress(1);
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case CPY_Absolute:  // CPY Absolute
      {
        if (cpu.isIndex8Bit())
        {
          execute8BitCPY(cpu);
        }
        else
        {
          execute16BitCPY(cpu);
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case CPY_DirectPage:  // CPY Direct Page
      {
        if (cpu.isIndex8Bit())
        {
          execute8BitCPY(cpu);
        }
        else
        {
          execute16BitCPY(cpu);
          cpu.addToCycles(1);
        }
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 3);
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

