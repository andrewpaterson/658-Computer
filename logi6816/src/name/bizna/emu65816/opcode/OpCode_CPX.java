package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_CPX
    extends OpCode
{
  public OpCode_CPX(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitCPX(Cpu65816 cpu)
  {
    int value = cpu.readByte(cpu.getAddressOfOpCodeData(getAddressingMode()));
    int result = toByte(Binary.lower8BitsOf(cpu.getX()) - value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
    boolean carry = Binary.lower8BitsOf(cpu.getX()) >= value;
    cpu.getCpuStatus().setCarryFlag(carry);
  }

  protected void execute16BitCPX(Cpu65816 cpu)
  {
    int value = cpu.readTwoBytes(cpu.getAddressOfOpCodeData(getAddressingMode()));
    int result = toShort(cpu.getX() - value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(result);
    boolean carry = cpu.getX() >= value;
    cpu.getCpuStatus().setCarryFlag(carry);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    switch (getCode())
    {
      case CPX_Immediate:  // CPX Immediate
      {
        if (cpu.indexIs8BitWide())
        {
          execute8BitCPX(cpu);
        }
        else
        {
          execute16BitCPX(cpu);
          cpu.addToProgramAddress(1);
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(2, 2);
        break;
      }
      case CPX_Absolute:  // CPX Absolute
      {
        if (cpu.indexIs8BitWide())
        {
          execute8BitCPX(cpu);
        }
        else
        {
          execute16BitCPX(cpu);
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddressAndCycles(3, 4);
        break;
      }
      case CPX_DirectPage:  // CPX Direct Page
      {
        if (cpu.indexIs8BitWide())
        {
          execute8BitCPX(cpu);
        }
        else
        {
          execute16BitCPX(cpu);
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

