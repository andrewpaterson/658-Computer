package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;

public class OpCode_CPY
    extends OpCode
{
  public OpCode_CPY(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitCPY(Cpu65816 cpu)
  {
    byte value = cpu.readByte(cpu.getAddressOfOpCodeData(getAddressingMode()));
    byte result = (byte) (Binary.lower8BitsOf(cpu.getY()) - value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    boolean carry = Binary.lower8BitsOf(cpu.getY()) >= value;
    cpu.getCpuStatus().setCarryFlag(carry);
  }

  protected void execute16BitCPY(Cpu65816 cpu)
  {
    short value = cpu.readTwoBytes(cpu.getAddressOfOpCodeData(getAddressingMode()));
    short result = (short) (cpu.getY() - value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    boolean carry = cpu.getY() >= value;
    cpu.getCpuStatus().setCarryFlag(carry);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case CPY_Immediate:  // CPY Immediate
      {
        if (cpu.indexIs8BitWide())
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
        if (cpu.indexIs8BitWide())
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
        if (cpu.indexIs8BitWide())
        {
          execute8BitCPY(cpu);
        }
        else
        {
          execute16BitCPY(cpu);
          cpu.addToCycles(1);
        }
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
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
}

