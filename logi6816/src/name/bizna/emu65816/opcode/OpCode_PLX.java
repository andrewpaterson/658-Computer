package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLX
    extends OpCode
{
  public OpCode_PLX(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.indexIs8BitWide())
    {
      int value = cpu.pull8Bit();
      cpu.setX(Binary.setLower8BitsOf16BitsValue(cpu.getX(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
      cpu.addToProgramAddressAndCycles(1, 4);
    }
    else
    {
      cpu.setX(cpu.pull16Bit());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getX());
      cpu.addToProgramAddressAndCycles(1, 5);
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

