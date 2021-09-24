package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_BRK
    extends OpCode
{
  public OpCode_BRK(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    if (cpu.getCpuStatus().emulationFlag())
    {
      cpu.getStack().push16Bit(toShort(cpu.getProgramAddress().getOffset() + 2));
      cpu.getCpuStatus().setBreakFlag(true);
      cpu.getStack().push8Bit(cpu.getCpuStatus().getRegisterValue());
      cpu.getCpuStatus().setInterruptDisableFlag(true);

      cpu.setProgramAddress(new Address(cpu.getEmulationInterrupts().brkIrq));
      cpu.addToCycles(7);
    }
    else
    {
      cpu.getStack().push8Bit(cpu.getProgramAddress().getBank());
      cpu.getStack().push16Bit(toShort(cpu.getProgramAddress().getOffset() + 2));
      cpu.getStack().push8Bit(cpu.getCpuStatus().getRegisterValue());
      cpu.getCpuStatus().setInterruptDisableFlag(true);
      cpu.getCpuStatus().setDecimalFlag(false);
      Address newAddress = new Address(cpu.getNativeInterrupts().brk);
      cpu.setProgramAddress(newAddress);
      cpu.addToCycles(8);
    }
  }
}

