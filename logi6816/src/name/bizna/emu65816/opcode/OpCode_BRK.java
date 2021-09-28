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
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.getCpuStatus().isEmulationMode())
    {
      cpu.push16Bit(toShort(cpu.getProgramCounter().getOffset() + 2));
      cpu.getCpuStatus().setBreakFlag(true);
      cpu.push8Bit(cpu.getCpuStatus().getRegisterValue());
      cpu.getCpuStatus().setInterruptDisableFlag(true);

      cpu.setProgramAddress(new Address(0x00, cpu.getEmulationInterrupts().brkIrq));
      cpu.addToCycles(7);
    }
    else
    {
      cpu.push8Bit(cpu.getProgramCounter().getBank());
      cpu.push16Bit(toShort(cpu.getProgramCounter().getOffset() + 2));
      cpu.push8Bit(cpu.getCpuStatus().getRegisterValue());
      cpu.getCpuStatus().setInterruptDisableFlag(true);
      cpu.getCpuStatus().setDecimalFlag(false);
      Address newAddress = new Address(0x00, cpu.getNativeInterrupts().brk);
      cpu.setProgramAddress(newAddress);
      cpu.addToCycles(8);
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

