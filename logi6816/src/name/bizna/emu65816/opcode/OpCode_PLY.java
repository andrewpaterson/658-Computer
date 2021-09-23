package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLY
    extends OpCode
{
  public OpCode_PLY(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.indexIs8BitWide())
    {
      int value = cpu.getStack().pull8Bit(cpu);
      cpu.setY(Binary.setLower8BitsOf16BitsValue(cpu.getY(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
      cpu.addToProgramAddressAndCycles(1, 4);
    }
    else
    {
      cpu.setY(cpu.getStack().pull16Bit(cpu));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getY());
      cpu.addToProgramAddressAndCycles(1, 5);
    }
  }
}

