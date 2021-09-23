package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_REP
    extends OpCode
{
  public OpCode_REP(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    byte value = cpu.readByte(cpu.getAddressOfOpCodeData(getAddressingMode()));
    byte statusByte = cpu.getCpuStatus().getRegisterValue();
    cpu.getCpuStatus().setRegisterValue((byte) (statusByte & ~value));
    cpu.addToProgramAddressAndCycles(2, 3);
  }
}

