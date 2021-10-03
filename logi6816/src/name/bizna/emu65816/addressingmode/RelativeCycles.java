package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Relative;

public class RelativeCycles
    extends InstructionCycles
{
  //20
  public RelativeCycles(Consumer<Cpu65816> operation)
  {
    super(Relative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_RL(), PC_inc(), E(operation)),  //Done if branch not taken
          new BusCycle(Address(PBR(), PC()), new NoteSix()),
          new BusCycle(Address(PBR(), PC()), PC_e(PBR(), PC(), R()), DONE()));
  }
}

