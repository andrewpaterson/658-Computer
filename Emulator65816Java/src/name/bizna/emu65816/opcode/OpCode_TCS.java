package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TCS
    extends OpCode
{
  public OpCode_TCS(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    int currentStackPointer = cpu.getStack().getStackPointer();
    if (cpu.getCpuStatus().emulationFlag())
    {
      currentStackPointer = Binary.setLower8BitsOf16BitsValue(currentStackPointer, Binary.lower8BitsOf(cpu.getA()));
    }
    else
    {
      currentStackPointer = cpu.getA();
    }
    cpu.clearStack(new Address(currentStackPointer));
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

