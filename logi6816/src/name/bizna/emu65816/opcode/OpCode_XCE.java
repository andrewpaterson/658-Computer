package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_XCE
    extends OpCode
{
  public OpCode_XCE(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    boolean oldCarry = cpu.getCpuStatus().carryFlag();
    boolean oldEmulation = cpu.getCpuStatus().emulationFlag();
    if (oldCarry)
    {
      cpu.getCpuStatus().setEmulationFlag();
    }
    else
    {
      cpu.getCpuStatus().clearEmulationFlag();
    }
    cpu.getCpuStatus().setCarryFlag(oldEmulation);

    cpu.setX((short) (cpu.getX() & 0xFF));
    cpu.setY((short) (cpu.getY() & 0xFF));

    if (cpu.getCpuStatus().emulationFlag())
    {
      cpu.getCpuStatus().setAccumulatorWidthFlag();
      cpu.getCpuStatus().setIndexWidthFlag();
    }
    else
    {
      cpu.getCpuStatus().clearAccumulatorWidthFlag();
      cpu.getCpuStatus().clearIndexWidthFlag();
    }

    // New stack
    cpu.clearStack();

    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

