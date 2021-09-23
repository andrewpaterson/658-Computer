package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLP
    extends OpCode
{
  public OpCode_PLP(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getCpuStatus().setRegisterValue(cpu.getStack().pull8Bit(cpu));
    cpu.addToProgramAddressAndCycles(1, 4);
  }
}

