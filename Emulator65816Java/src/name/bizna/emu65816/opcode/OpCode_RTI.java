package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_RTI
    extends OpCode
{
  public OpCode_RTI(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    // Note: The picture in the 65816 programming manual about this looks wrong.
    // This implementation follows the text instead.
    cpu.getCpuStatus().setRegisterValue(cpu.getStack().pull8Bit());

    if (cpu.getCpuStatus().emulationFlag())
    {
      Address newProgramAddress = new Address(cpu.getProgramAddress().getBank(), cpu.getStack().pull16Bit());
      cpu.setProgramAddress(newProgramAddress);
      cpu.addToCycles(6);
    }
    else
    {
      int offset = cpu.getStack().pull16Bit();
      int bank = cpu.getStack().pull8Bit();
      Address newProgramAddress = new Address(bank, offset);
      cpu.setProgramAddress(newProgramAddress);
      cpu.addToCycles(7);
    }
  }
}

