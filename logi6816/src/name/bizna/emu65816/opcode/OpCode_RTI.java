package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_RTI
    extends OpCode
{
  public OpCode_RTI(int mCode, InstructionCycles cycles)
  {
    super("RTI", "Return from Interrupt", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    // Note: The picture in the 65816 programming manual about this looks wrong.
    // This implementation follows the text instead.
    cpu.getCpuStatus().setRegisterValue(cpu.pull8Bit());

    if (cpu.getCpuStatus().isEmulationMode())
    {
      Address newProgramAddress = new Address(cpu.getProgramCounter().getBank(), cpu.pull16Bit());
      cpu.setProgramAddress(newProgramAddress);
      cpu.addToCycles(6);
    }
    else
    {
      int offset = cpu.pull16Bit();
      int bank = cpu.pull8Bit();
      Address newProgramAddress = new Address(bank, offset);
      cpu.setProgramAddress(newProgramAddress);
      cpu.addToCycles(7);
    }
  }
}

