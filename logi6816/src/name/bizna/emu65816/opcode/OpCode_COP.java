package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_COP
    extends OpCode
{
  public OpCode_COP(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.getCpuStatus().emulationFlag())
    {
      cpu.getStack().push16Bit((short) (cpu.getProgramAddress().getOffset() + 2));
      cpu.getStack().push8Bit(cpu.getCpuStatus().getRegisterValue());
      cpu.getCpuStatus().setInterruptDisableFlag();
      cpu.setProgramAddress(new Address(cpu.getEmulationInterrupts().coProcessorEnable));
      cpu.addToCycles(7);
    }
    else
    {
      cpu.getStack().push8Bit(cpu.getProgramAddress().getBank());
      cpu.getStack().push16Bit((short) (cpu.getProgramAddress().getOffset() + 2));
      cpu.getStack().push8Bit(cpu.getCpuStatus().getRegisterValue());
      cpu.getCpuStatus().setInterruptDisableFlag();
      cpu.setProgramAddress(new Address(cpu.getNativeInterrupts().coProcessorEnable));
      cpu.addToCycles(8);
    }
  }
}
