package net.logicim.ui.circuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.geometry.LineMinimiser;
import net.logicim.common.geometry.LinePositionCache;
import net.logicim.common.geometry.LineSplitter;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Positions;
import net.logicim.data.circuit.SubcircuitData;
import net.logicim.data.circuit.SubcircuitEditorData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulations;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.LineOverlap;
import net.logicim.ui.common.TraceOverlap;
import net.logicim.ui.common.integratedcircuit.*;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.TraceFinder;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.connection.LocalMultiSimulationConnectionNet;
import net.logicim.ui.connection.PortTraceFinder;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.ConnectionViewCache;
import net.logicim.ui.simulation.StaticViewIterator;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.*;

public class SubcircuitView
{
  protected String typeName;  //The subcircuit instance name is on the SubcircuitInstanceView.

  protected Set<IntegratedCircuitView<?, ?>> integratedCircuitViews;
  protected Set<PassiveView<?, ?>> passiveViews;
  protected Set<DecorativeView<?>> decorativeViews;
  protected Set<SubcircuitInstanceView> subcircuitInstanceViews;

  protected Set<TraceView> traceViews;
  protected Set<TunnelView> tunnelViews;
  protected Map<String, Set<TunnelView>> tunnelViewsMap;

  protected SubcircuitSimulations simulations;

  protected ConnectionViewCache connectionViewCache;

  public SubcircuitView()
  {
    this.integratedCircuitViews = new LinkedHashSet<>();
    this.traceViews = new LinkedHashSet<>();
    this.tunnelViewsMap = new LinkedHashMap<>();
    this.passiveViews = new LinkedHashSet<>();
    this.subcircuitInstanceViews = new LinkedHashSet<>();
    this.tunnelViews = new LinkedHashSet<>();
    this.decorativeViews = new LinkedHashSet<>();
    this.connectionViewCache = new ConnectionViewCache();
    this.simulations = new SubcircuitSimulations();
  }

  public List<View> getAllViews()
  {
    List<View> views = new ArrayList<>(traceViews);
    views.addAll(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(subcircuitInstanceViews);
    views.addAll(integratedCircuitViews);
    return views;
  }

  public List<StaticView<?>> getStaticViews()
  {
    List<StaticView<?>> views = new ArrayList<>(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    return views;
  }

  public List<SubcircuitInstanceView> getSubcircuitInstanceViews()
  {
    return new ArrayList<>(subcircuitInstanceViews);
  }

  public List<SubcircuitInstanceView> getSubcircuitInstanceViews(SubcircuitView instanceSubcircuitView)
  {
    ArrayList<SubcircuitInstanceView> result = new ArrayList<>();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      if (subcircuitInstanceView.getInstanceSubcircuitView() == instanceSubcircuitView)
      {
        result.add(subcircuitInstanceView);
      }
    }
    return result;
  }

  public void disconnectTraceView(TraceView traceView)
  {
    List<ConnectionView> connectionViews = traceView.getConnectionViews();
    connectionViewCache.removeAll(traceView, connectionViews);
    traceView.disconnectViews();
  }

  public List<ConnectionView> disconnectStaticView(StaticView<?> staticView, CircuitSimulation circuitSimulation)
  {
    if (staticView == null)
    {
      throw new SimulatorException("Cannot disconnect [null] view.");
    }

    List<ConnectionView> connectionViews = staticView.getConnectionViews();
    if (connectionViews == null)
    {
      throw new SimulatorException("Cannot disconnect %s with [null] connections.", staticView.toIdentifierString());
    }

    connectionViewCache.removeAll(staticView, connectionViews);
    staticView.disconnectView(circuitSimulation);

    return connectionViews;
  }

  public void deleteComponentView(StaticView<?> staticView, CircuitSimulation circuitSimulation, SubcircuitSimulation subcircuitSimulation)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);
    deleteComponentViews(staticViews, circuitSimulation, subcircuitSimulation);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews, CircuitSimulation circuitSimulation, SubcircuitSimulation subcircuitSimulation)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> componentView : staticViews)
    {
      if (componentView == null)
      {
        throw new SimulatorException("Cannot delete a [null] view.");
      }

      List<ConnectionView> disconnectedConnectionViews = disconnectStaticView(componentView, circuitSimulation);
      for (ConnectionView connectionView : disconnectedConnectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("Disconnected connection may not be null from component [%s].", componentView.toIdentifierString());
        }
      }

      connectionViews.addAll(disconnectedConnectionViews);
      if (componentView instanceof IntegratedCircuitView)
      {
        removeIntegratedCircuitView((IntegratedCircuitView<?, ?>) componentView);
      }
      else if (componentView instanceof PassiveView)
      {
        removePassiveView((PassiveView<?, ?>) componentView);
      }
      else if (componentView instanceof SubcircuitInstanceView)
      {
        removeSubcircuitInstanceView((SubcircuitInstanceView) componentView);
      }
      else if (componentView instanceof DecorativeView)
      {
        removeDecorativeView((DecorativeView<?>) componentView);
      }
      else if (componentView instanceof TunnelView)
      {
        removeTunnelView((TunnelView) componentView);
      }
      else
      {
        throw new SimulatorException("Cannot delete view of class [%s].", componentView.getClass().getSimpleName());
      }
    }

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(connectionViews, subcircuitSimulation);
    fireConnectionEvents(updatedConnectionViews, subcircuitSimulation);
  }

  public void fireConnectionEvents(Set<ConnectionView> connectionViews,
                                   SubcircuitSimulation subcircuitSimulation)
  {
    Set<PortView> portViews = getPortViews(connectionViews);
    for (PortView portView : portViews)
    {
      portView.traceConnected(subcircuitSimulation);
    }
  }

  public void deleteTraceViews(Set<TraceView> inputTraceViews, SubcircuitSimulation subcircuitSimulation)
  {
    TraceFinder traceFinder = new TraceFinder();
    for (TraceView traceView : inputTraceViews)
    {
      List<TraceOverlap> tracesTouching = getTracesTouching(traceView.getLine());
      for (TraceOverlap traceOverlap : tracesTouching)
      {
        TraceView touchingTraceView = traceOverlap.getTraceView();
        if (!inputTraceViews.contains(touchingTraceView))
        {
          traceFinder.add(touchingTraceView);
        }
      }
    }

    Set<ConnectionView> nonTraceConnectionViews = findNonTraceConnections(inputTraceViews);
    removeTraceViews(inputTraceViews);

    Set<Line> lines = new LinkedHashSet<>();
    traceFinder.process();
    Set<TraceView> touchingTraceViews = traceFinder.getTraceViews();
    recreateTraceViews(lines, touchingTraceViews, subcircuitSimulation);

    findAndConnectNonTraceViewsForDeletion(subcircuitSimulation, nonTraceConnectionViews);
  }

  public ConnectionView getOrAddConnectionView(Int2D position, View view)
  {
    return connectionViewCache.getOrAddConnectionView(position, view);
  }

  public StaticViewIterator staticViewIterator()
  {
    return new StaticViewIterator(tunnelViews, integratedCircuitViews, passiveViews, subcircuitInstanceViews, decorativeViews);
  }

  public Set<TraceView> getTraceViews()
  {
    return traceViews;
  }

  public void validate()
  {
    validateConnectionViews();
    validateTracesContainOnlyCurrentViews();
    validateTunnelViews();

    validateComponentSimulations();
  }

  protected void validateComponentSimulations()
  {
    validateComponentSimulations(subcircuitInstanceViews, "subcircuit instance view");
    validateComponentSimulations(integratedCircuitViews, "integrated circuit view");
    validateComponentSimulations(passiveViews, "passive view");

    validateWireSimulations(traceViews, "trace view");
    validateWireSimulations(tunnelViews, "tunnel view");
  }

  protected void validateComponentSimulations(Set<? extends ComponentView<?>> componentViews, String type)
  {
    for (ComponentView<?> componentView : componentViews)
    {
      Set<SubcircuitInstanceSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitInstanceSimulations());
      Set<SubcircuitSimulation> componentSubcircuitSimulations = componentView.getComponentSubcircuitSimulations();
      if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
      {
        throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all %s [%s] simulations (of count [%s]).",
                                     getTypeName(),
                                     simulationSubcircuitSimulations.size(),
                                     type,
                                     componentView.getDescription(),
                                     componentSubcircuitSimulations);
      }

      validatePortSimulations(componentView.getPortViews());
    }

  }

  protected void validatePortSimulations(List<PortView> portViews)
  {
    for (PortView portView : portViews)
    {
      Set<SubcircuitInstanceSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitInstanceSimulations());
      Set<SubcircuitSimulation> componentSubcircuitSimulations = portView.getPortSubcircuitSimulations();
      if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
      {
        throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all port view [%s] simulations (of count [%s]).",
                                     getTypeName(),
                                     simulationSubcircuitSimulations.size(),
                                     portView.getDescription(),
                                     componentSubcircuitSimulations);
      }
    }
  }

  protected void validateWireSimulations(Set<? extends WireView> wireViews, String type)
  {
    for (WireView wireView : wireViews)
    {
      Set<SubcircuitInstanceSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitInstanceSimulations());
      Set<SubcircuitSimulation> componentSubcircuitSimulations = wireView.getWireSubcircuitSimulations();
      if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
      {
        throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all %s [%s] simulations (of count [%s]).",
                                     getTypeName(),
                                     simulationSubcircuitSimulations.size(),
                                     type,
                                     wireView.getDescription(),
                                     componentSubcircuitSimulations);
      }
    }
  }

  private void validateConnectionViews()
  {
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      if (staticView instanceof ComponentView)
      {
        ComponentView<?> componentView = (ComponentView<?>) staticView;
        List<PortView> portViews = componentView.getPortViews();
        for (PortView portView : portViews)
        {
          Int2D portPosition = portView.getGridPosition();
          ConnectionView connectionView = portView.getConnection();
          if (connectionView != null)
          {
            Int2D connectionPosition = connectionView.getGridPosition();
            if (connectionPosition == null)
            {
              throw new SimulatorException("Position on connection on %s cannot be null.", componentView.toIdentifierString());
            }
            if (!portPosition.equals(connectionPosition))
            {
              throw new SimulatorException("%s port [%s] position (%s) must be equal to connection position (%s).",
                                           componentView.toIdentifierString(),
                                           portView.getText(),
                                           Int2D.toString(portPosition),
                                           Int2D.toString(connectionPosition));
            }
          }
        }
      }

      List<ConnectionView> localConnectionViews = staticView.getConnectionViews();
      for (ConnectionView connectionView : localConnectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("Connection on %s cannot be null.", staticView.toIdentifierString());
        }
        Int2D connectionPosition = connectionView.getGridPosition();
        if (connectionPosition == null)
        {
          throw new SimulatorException("Position on connection on %s cannot be null.", staticView.toIdentifierString());
        }

        ConnectionView cacheConnectionView = connectionViewCache.getConnectionView(connectionPosition);
        if (cacheConnectionView != connectionView)
        {
          throw new SimulatorException("%s connection (%s) must be equal to cache connection (%s).",
                                       staticView.toIdentifierString(),
                                       Int2D.toString(connectionPosition),
                                       ConnectionView.toPositionString(cacheConnectionView));
        }
      }
    }

    connectionViewCache.validatePositions();
  }

  protected void validateTunnelViews()
  {
    for (TunnelView tunnelView : tunnelViews)
    {
      if (!tunnelView.isRemoved())
      {
        String name = tunnelView.getName();
        String sanitisedName = name.trim().toLowerCase();
        if (!tunnelView.getSanitisedName().equals(sanitisedName))
        {
          throw new SimulatorException("Tunnel view sanitised name is [%s] but should be [%s].", tunnelView.getSanitisedName(), sanitisedName);
        }

        Set<TunnelView> tunnelViews = tunnelViewsMap.get(sanitisedName);
        if (!sanitisedName.isEmpty())
        {
          if (tunnelViews == null)
          {
            throw new SimulatorException("TunnelViewsMap did not contain a map for name [%s]", tunnelView.getName());
          }
          if (!tunnelViews.contains(tunnelView))
          {
            throw new SimulatorException("TunnelViewsMap did not contain tunnel view [%s].", tunnelView.getName());
          }
        }
      }
    }

    for (String sanitisedName : tunnelViewsMap.keySet())
    {
      Set<TunnelView> tunnelViews = tunnelViewsMap.get(sanitisedName);
      for (TunnelView tunnelView : tunnelViews)
      {
        if (!this.tunnelViews.contains(tunnelView))
        {
          throw new SimulatorException("TunnelViews did not contain tunnel view [%s].", tunnelView.getName());
        }
      }
    }
  }

  protected void validateTracesContainOnlyCurrentViews()
  {
    for (TraceView traceView : traceViews)
    {
      if (traceView.hasConnections())
      {
        ConnectionView startConnection = traceView.getStartConnection();
        for (View view : startConnection.getConnectedComponents())
        {
          boolean contained = true;
          if (view instanceof IntegratedCircuitView)
          {
            if (!integratedCircuitViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof TraceView)
          {
            if (!traceViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof PassiveView)
          {
            if (!passiveViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof SubcircuitInstanceView)
          {
            if (!subcircuitInstanceViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof DecorativeView)
          {
            if (!decorativeViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof TunnelView)
          {
            if (!tunnelViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view == null)
          {
            throw new SimulatorException("TraceView [" + traceView.getDescription() + "] does not include trace has null connection.");
          }
          else
          {
            throw new SimulatorException(view.getDescription() + " referenced by TraceView [" + traceView.getDescription() + "] has not been included in validateConsistency.");
          }

          if (!contained)
          {
            throw new SimulatorException(view.getDescription() + " referenced by TraceView [" + traceView.getDescription() + "] does not include trace.");
          }
        }
      }
    }
  }

  public void addIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.add(integratedCircuitView);
    }
  }

  public void removeIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.remove(integratedCircuitView);
    }
  }

  public void addPassiveView(PassiveView<?, ?> view)
  {
    synchronized (this)
    {
      passiveViews.add(view);
    }
  }

  public void addSubcircuitInstanceView(SubcircuitInstanceView view)
  {
    synchronized (this)
    {
      subcircuitInstanceViews.add(view);
    }
  }

  public void addDecorativeView(DecorativeView<?> view)
  {
    synchronized (this)
    {
      decorativeViews.add(view);
    }
  }

  protected boolean removePassiveView(PassiveView<?, ?> passiveView)
  {
    synchronized (this)
    {
      return passiveViews.remove(passiveView);
    }
  }

  protected boolean removeSubcircuitInstanceView(SubcircuitInstanceView subcircuitInstanceView)
  {
    synchronized (this)
    {
      return subcircuitInstanceViews.remove(subcircuitInstanceView);
    }
  }

  protected boolean removeDecorativeView(DecorativeView<?> decorativeView)
  {
    synchronized (this)
    {
      return decorativeViews.remove(decorativeView);
    }
  }

  public void addTraceView(TraceView view)
  {
    synchronized (this)
    {
      traceViews.add(view);
    }
  }

  public void removeTraceView(TraceView traceView)
  {
    if (!traceView.hasConnections())
    {
      synchronized (this)
      {
        if (!traceViews.remove(traceView))
        {
          throw new SimulatorException("Cannot remove trace not in circuit editor.");
        }
      }
    }
    else
    {
      throw new SimulatorException("Cannot remove trace view with connections.");
    }
  }

  protected boolean removeTunnelView(TunnelView tunnelView)
  {
    synchronized (this)
    {
      String name = tunnelView.getSanitisedName();
      boolean removed = this.tunnelViews.remove(tunnelView);
      if (!name.isEmpty())
      {
        Set<TunnelView> tunnelViews = this.tunnelViewsMap.get(name);
        tunnelViews.remove(tunnelView);
        if (tunnelViews.isEmpty())
        {
          this.tunnelViewsMap.remove(name);
        }
      }
      return removed;
    }
  }

  public Set<TunnelView> addTunnel(TunnelView tunnelView)
  {
    synchronized (this)
    {
      this.tunnelViews.add(tunnelView);

      String name = tunnelView.getSanitisedName();
      if (!name.isEmpty())
      {
        Set<TunnelView> tunnelViews = this.tunnelViewsMap.get(name);
        if (tunnelViews == null)
        {
          tunnelViews = new LinkedHashSet<>();
          this.tunnelViewsMap.put(name, tunnelViews);
        }
        tunnelViews.add(tunnelView);
        return tunnelViews;
      }

      return null;
    }
  }

  public ConnectionView getConnection(int x, int y)
  {
    return connectionViewCache.getConnectionView(x, y);
  }

  public Set<ConnectionView> createTracesForConnectionViews(Collection<ConnectionView> connectionViews,
                                                            SubcircuitSimulation startingSubcircuitSimulation)
  {
    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    if (startingSubcircuitSimulation != null)
    {
      for (ConnectionView connectionView : connectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("Connection may not be null.");
        }

        if (!updatedConnectionViews.contains(connectionView))
        {
          List<LocalMultiSimulationConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(startingSubcircuitSimulation, connectionView);
          List<ConnectionView> connectionNetConnectionViews = PortTraceFinder.getConnectionViews(connectionNets);
          updatedConnectionViews.addAll(connectionNetConnectionViews);
        }
      }
    }
    return updatedConnectionViews;
  }

  public void removeTraceViews(Collection<TraceView> traceViews)
  {
    for (TraceView traceView : traceViews)
    {
      disconnectTraceView(traceView);
      removeTraceView(traceView);
    }
  }

  public List<ConnectionView> getOrCreateStaticViewConnections(List<StaticView<?>> staticViews)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.getOrCreateConnectionViews(this));
    }
    return connectionViews;
  }

  public List<ConnectionView> getConnectionViews(List<StaticView<?>> staticViews,
                                                 List<SubcircuitInstanceView> subcircuitInstanceViews,
                                                 Set<TraceView> traceViews)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.getConnectionViews());
    }
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      connectionViews.addAll(subcircuitInstanceView.getConnectionViews());
    }
    for (TraceView traceView : traceViews)
    {
      connectionViews.addAll(traceView.getConnectionViews());
    }
    return connectionViews;
  }

  public void enableStaticViews(List<StaticView<?>> staticViews)
  {
    for (StaticView<?> staticView : staticViews)
    {
      staticView.enable();
    }
  }

  private List<Component> createComponents(List<StaticView<?>> staticViews, List<SubcircuitInstanceView> subcircuitInstanceViews, SubcircuitSimulation subcircuitSimulation)
  {
    List<Component> components = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      Component component = staticView.createComponent(subcircuitSimulation);
      components.add(component);
    }

    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      Component component = subcircuitInstanceView.createSubcircuitInstance(subcircuitSimulation);
      components.add(component);
    }
    return components;
  }

  public void simulationStarted(List<StaticView<?>> staticViews, SubcircuitSimulation simulation)
  {
    for (StaticView<?> staticView : staticViews)
    {
      staticView.simulationStarted(simulation);
    }
  }

  public Set<PortView> getPortViews(Set<ConnectionView> connectionViews)
  {
    Set<PortView> portViews = new HashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      List<View> connectedComponents = connectionView.getConnectedComponents();
      for (View view : connectedComponents)
      {
        if (view instanceof ComponentView)
        {
          ComponentView<?> componentView = (ComponentView<?>) view;
          PortView portView = componentView.getPortView(connectionView);
          portViews.add(portView);
        }
      }
    }
    return portViews;
  }

  public List<TraceView> getTraceViewsInGridSpace(int x, int y)
  {
    List<TraceView> traceViews = new ArrayList<>();
    for (TraceView traceView : getTraceViews())
    {
      if (traceView.contains(x, y))
      {
        traceViews.add(traceView);
      }
    }
    return traceViews;
  }

  public List<TraceOverlap> getTracesTouching(Line line)
  {
    List<TraceOverlap> overlaps = new ArrayList<>();
    for (TraceView traceView : getTraceViews())
    {
      LineOverlap overlap = traceView.touches(line);
      if (overlap != LineOverlap.None)
      {
        overlaps.add(new TraceOverlap(overlap, traceView));
      }
    }
    return overlaps;
  }

  public Set<TraceView> createTraceViews(List<Line> inputLines, SubcircuitSimulation subcircuitSimulation)
  {
    Set<Line> lines = new LinkedHashSet<>(inputLines);
    TraceFinder traceFinder = new TraceFinder();
    for (Line line : inputLines)
    {
      List<TraceOverlap> tracesTouching = getTracesTouching(line);
      for (TraceOverlap traceOverlap : tracesTouching)
      {
        traceFinder.add(traceOverlap.getTraceView());
      }
    }

    traceFinder.process();
    Set<TraceView> touchingTraceViews = traceFinder.getTraceViews();
    return recreateTraceViews(lines, touchingTraceViews, subcircuitSimulation);
  }

  protected Set<TraceView> recreateTraceViews(Set<Line> lines,
                                              Set<TraceView> touchingTraceViews,
                                              SubcircuitSimulation subcircuitSimulation)
  {
    for (TraceView traceView : touchingTraceViews)
    {
      lines.add(traceView.getLine());
    }
    removeTraceViews(touchingTraceViews);
    Set<TraceView> traceViews = generateNewTraces(lines);

    Set<ConnectionView> updatedConnectionViews = connectCreatedTraceViews(traceViews, subcircuitSimulation);
    fireConnectionEvents(updatedConnectionViews, subcircuitSimulation);
    return traceViews;
  }

  protected void findAndConnectNonTraceViewsForDeletion(SubcircuitSimulation subcircuitSimulation,
                                                        Set<ConnectionView> nonTraceConnectionViews)
  {
    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    for (ConnectionView nonTraceConnectionView : nonTraceConnectionViews)
    {
      List<LocalMultiSimulationConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(subcircuitSimulation, nonTraceConnectionView);
      updatedConnectionViews.addAll(PortTraceFinder.getConnectionViews(connectionNets));
    }
    fireConnectionEvents(updatedConnectionViews, subcircuitSimulation);
  }

  protected Set<ConnectionView> findNonTraceConnections(Set<TraceView> inputTraceViews)
  {
    Set<ConnectionView> nonTraceConnectionViews = new LinkedHashSet<>();
    for (TraceView inputTraceView : inputTraceViews)
    {
      List<ConnectionView> connectionViews = inputTraceView.getConnectionViews();
      for (ConnectionView connectionView : connectionViews)
      {
        List<View> connectedComponents = connectionView.getConnectedComponents();
        for (View connectedComponent : connectedComponents)
        {
          if (!(connectedComponent instanceof TraceView))
          {
            nonTraceConnectionViews.add(connectionView);
          }
        }
      }
    }
    return nonTraceConnectionViews;
  }

  private Set<TraceView> generateNewTraces(Set<Line> lines)
  {
    Set<Line> mergedLines = LineMinimiser.minimise(lines);
    Positions positionMap = new Positions(lines);

    LinePositionCache lineCache = new LinePositionCache(mergedLines);
    StaticViewIterator iterator = staticViewIterator();

    List<Int2D> additionalJunctions = new ArrayList<>();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      List<ConnectionView> connections = staticView.getConnectionViews();
      for (ConnectionView connection : connections)
      {
        Int2D position = connection.getGridPosition();
        if (lineCache.touchesLine(position.x, position.y))
        {
          additionalJunctions.add(position.clone());
        }
      }
    }

    Set<Line> splitLines = LineSplitter.split(lineCache, positionMap, additionalJunctions);

    Set<TraceView> traceViews = new LinkedHashSet<>();
    for (Line line : splitLines)
    {
      traceViews.add(new TraceView(this, line));
    }
    return traceViews;
  }

  public Set<ConnectionView> connectCreatedTraceViews(Set<TraceView> traceViews,
                                                      SubcircuitSimulation subcircuitSimulation)
  {
    Set<ConnectionView> connectionViews = new HashSet<>();
    Set<ConnectionView> updatedConnectionViews;
    int i = 0;
    for (TraceView traceView : traceViews)
    {
      if (!traceView.hasConnections())
      {
        throw new SimulatorException("Cannot finalise a removed Trace.  Iteration [%s].", i);
      }
      connectionViews.add(traceView.getStartConnection());
      i++;
    }
    updatedConnectionViews = createTracesForConnectionViews(connectionViews, subcircuitSimulation);
    return updatedConnectionViews;
  }

  public SubcircuitEditorData save(Set<View> selection, long id)
  {
    ArrayList<StaticData<?>> staticDatas = new ArrayList<>();
    ArrayList<SubcircuitInstanceData> subcircuitInstanceDatas = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      StaticData<?> staticData = (StaticData<?>) staticView.save(selection.contains(staticView));
      if (staticData == null)
      {
        throw new SimulatorException("%s save may not return null.", staticView.toIdentifierString());
      }
      else if (staticData instanceof SubcircuitInstanceData)
      {
        subcircuitInstanceDatas.add((SubcircuitInstanceData) staticData);
      }
      else
      {
        staticDatas.add(staticData);
      }
    }

    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceData traceData = traceView.save(selection.contains(traceView));
      traceDatas.add(traceData);
    }

    SubcircuitData subcircuitData = new SubcircuitData(staticDatas, subcircuitInstanceDatas, traceDatas);
    return new SubcircuitEditorData(subcircuitData, typeName, id);
  }

  public void startMoveComponents(List<StaticView<?>> staticViews,
                                  List<TraceView> traceViews,
                                  SubcircuitSimulation subcircuitSimulation)
  {
    List<TraceView> connectedTraceViews = findImmediateConnectedTraceViews(staticViews);

    Set<TraceView> allConnectedTraceViews = new HashSet<>(connectedTraceViews);
    allConnectedTraceViews.removeAll(traceViews);

    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    CircuitSimulation circuitSimulation = subcircuitSimulation.getCircuitSimulation();
    for (StaticView<?> staticView : staticViews)
    {
      staticView.disable();
      connectionViews.addAll(disconnectStaticView(staticView, circuitSimulation));
      staticView.destroyComponent(circuitSimulation);
    }

    for (TraceView traceView : traceViews)
    {
      connectionViews.addAll(traceView.getConnectionViews());
      disconnectTraceView(traceView);
    }

    createTracesForConnectionViews(connectionViews, subcircuitSimulation);

    recreateTraceViews(new HashSet<>(), allConnectedTraceViews, subcircuitSimulation);
  }

  public List<View> doneMoveComponents(SubcircuitSimulation subcircuitSimulation,
                                       List<StaticView<?>> componentViews,
                                       List<TraceView> traceViews,
                                       Set<StaticView<?>> selectedViews)
  {
    List<Line> newLines = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      newLines.add(traceView.getLine());
    }
    removeTraceViews(new LinkedHashSet<>(traceViews));

    List<ConnectionView> createdConnectionViews = getOrCreateStaticViewConnections(componentViews);
    Set<ConnectionView> junctions = getComponentConnectionPositions(componentViews);
    Set<TraceView> existingTraceViews = new LinkedHashSet<>();
    for (ConnectionView junction : junctions)
    {
      Int2D position = junction.getGridPosition();
      existingTraceViews.addAll(getTraceViewsInGridSpace(position.x, position.y));
    }

    List<Line> existingLines = new ArrayList<>();
    for (TraceView existingTraceView : existingTraceViews)
    {
      existingLines.add(existingTraceView.getLine());
    }
    removeTraceViews(existingTraceViews);

    enableStaticViews(componentViews);

    List<StaticView<?>> staticViews = new ArrayList<>();
    List<SubcircuitInstanceView> subcircuitInstanceViews = new ArrayList<>();
    for (StaticView<?> componentView : componentViews)
    {
      if (componentView instanceof SubcircuitInstanceView)
      {
        subcircuitInstanceViews.add((SubcircuitInstanceView) componentView);
      }
      else
      {
        staticViews.add(componentView);
      }
    }

    createComponents(staticViews, subcircuitInstanceViews, subcircuitSimulation);

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(createdConnectionViews, subcircuitSimulation);
    simulationStarted(componentViews, subcircuitSimulation);

    Set<TraceView> existingTraces = createTraceViews(existingLines, subcircuitSimulation);
    Set<ConnectionView> updatedCreatedTraceConnectionViews = connectCreatedTraceViews(existingTraces, subcircuitSimulation);
    updatedConnectionViews.addAll(updatedCreatedTraceConnectionViews);

    Set<TraceView> newTraces = createTraceViews(newLines, subcircuitSimulation);
    updatedConnectionViews.addAll(connectCreatedTraceViews(newTraces, subcircuitSimulation));

    fireConnectionEvents(updatedConnectionViews, subcircuitSimulation);

    return calculateNewSelection(componentViews, selectedViews, newTraces);
  }

  protected List<View> calculateNewSelection(List<StaticView<?>> staticViews,
                                             Set<StaticView<?>> selectedViews,
                                             Set<TraceView> newTraces)
  {
    List<View> newSelection = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      if (selectedViews.contains(staticView))
      {
        newSelection.add(staticView);
      }
    }
    newSelection.addAll(newTraces);

    return newSelection;
  }

  protected List<TraceView> findImmediateConnectedTraceViews(List<StaticView<?>> staticViews)
  {
    List<TraceView> connectedTraceViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      List<ConnectionView> connectionViews = staticView.getConnectionViews();
      for (ConnectionView connectionView : connectionViews)
      {
        List<View> connectedComponents = connectionView.getConnectedComponents();
        for (View connectedComponent : connectedComponents)
        {
          if (connectedComponent instanceof TraceView)
          {
            connectedTraceViews.add((TraceView) connectedComponent);
          }
        }
      }
    }
    return connectedTraceViews;
  }

  protected Set<ConnectionView> getComponentConnectionPositions(List<StaticView<?>> staticViews)
  {
    Set<ConnectionView> junctions = new HashSet<>();
    for (StaticView<?> staticView : staticViews)
    {
      List<ConnectionView> connectionViews = staticView.getConnectionViews();
      for (ConnectionView connectionView : connectionViews)
      {
        junctions.add(connectionView);
      }
    }
    return junctions;
  }

  public List<View> getSelectionFromRectangle(Float2D start,
                                              Float2D end)
  {
    boolean includeIntersections = start.x > end.x;

    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<View> selectedViews = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      updateSelectedViews(start,
                          end,
                          includeIntersections,
                          boundBoxPosition,
                          boundBoxDimension,
                          selectedViews,
                          staticView);
    }

    for (TraceView traceView : traceViews)
    {
      Line line = traceView.getLine();
      line.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (line.isPositionOn(new Int2D(start)))
        {
          selectedViews.add(traceView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(traceView);
        }
      }
    }
    return selectedViews;
  }

  protected void updateSelectedViews(Float2D start,
                                     Float2D end,
                                     boolean includeIntersections,
                                     Float2D boundBoxPosition,
                                     Float2D boundBoxDimension,
                                     List<View> selectedViews,
                                     StaticView<?> componentView)
  {
    if (componentView.isEnabled())
    {
      componentView.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (BoundingBox.containsPoint(new Int2D(start), boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(componentView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(componentView);
        }
      }
    }
  }

  protected boolean isPoint(Float2D start, Float2D end)
  {
    return (Math.round(start.x) == Math.round(end.x)) &&
           (Math.round(start.y) == Math.round(end.y));
  }

  public String getTypeName()
  {
    return typeName;
  }

  @Override
  public String toString()
  {
    return typeName;
  }

  public void setTypeName(String typeName)
  {
    this.typeName = typeName;
  }

  public List<PinView> findAllPins()
  {
    ArrayList<PinView> pinViews = new ArrayList<>();
    for (PassiveView<?, ?> passiveView : passiveViews)
    {
      if (passiveView instanceof PinView)
      {
        pinViews.add((PinView) passiveView);
      }
    }
    return pinViews;
  }

  public Set<SubcircuitInstanceView> findAllSubcircuitInstanceViews()
  {
    return subcircuitInstanceViews;
  }

  public Set<StaticView<?>> findAllViewsOfClass(Class<? extends StaticView<?>> viewClass)
  {
    Set<StaticView<?>> views = new LinkedHashSet<>();
    if (DecorativeView.class.isAssignableFrom(viewClass))
    {
      for (DecorativeView<?> decorativeView : decorativeViews)
      {
        if (viewClass.isAssignableFrom(decorativeView.getClass()))
        {
          views.add(decorativeView);
        }
      }
    }
    else if (IntegratedCircuitView.class.isAssignableFrom(viewClass))
    {
      for (IntegratedCircuitView<?, ?> integratedCircuitView : integratedCircuitViews)
      {
        if (viewClass.isAssignableFrom(integratedCircuitView.getClass()))
        {
          views.add(integratedCircuitView);
        }
      }
    }
    else if (PassiveView.class.isAssignableFrom(viewClass))
    {
      for (PassiveView<?, ?> passiveView : passiveViews)
      {
        if (viewClass.isAssignableFrom(passiveView.getClass()))
        {
          views.add(passiveView);
        }
      }
    }
    else if (SubcircuitInstanceView.class.isAssignableFrom(viewClass))
    {
      for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
      {
        if (viewClass.isAssignableFrom(subcircuitInstanceView.getClass()))
        {
          views.add(subcircuitInstanceView);
        }
      }
    }
    return views;
  }

  public List<String> getSubcircuitInstanceNames()
  {
    Set<String> names = new HashSet<>();
    Set<SubcircuitInstanceView> subcircuitInstanceViews = findAllSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      names.add(subcircuitInstanceView.getTypeName());
    }
    return new ArrayList<>(names);
  }

  public List<String> getTypeNameAsList()
  {
    ArrayList<String> list = new ArrayList<>();
    list.add(getTypeName());
    return list;
  }

  public void destroyComponentsAndSimulations(CircuitSimulation circuitSimulation)
  {
    destroyComponents(circuitSimulation, getStaticViews(), traceViews, subcircuitInstanceViews);

    simulations.clear();
  }

  protected void destroyComponents(CircuitSimulation circuitSimulation,
                                   Collection<StaticView<?>> staticViews,
                                   Collection<TraceView> traceViews,
                                   Collection<SubcircuitInstanceView> subcircuitInstanceViews)
  {
    for (StaticView<?> staticView : staticViews)
    {
      staticView.destroyComponent(circuitSimulation);
    }

    for (TraceView traceView : traceViews)
    {
      traceView.destroyComponent(circuitSimulation);
    }

    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      subcircuitInstanceView.destroyComponent(circuitSimulation);
    }
  }

  public List<Component> createComponents(SubcircuitSimulation subcircuitSimulation)
  {
    List<StaticView<?>> staticViews = getStaticViews();
    List<SubcircuitInstanceView> subcircuitInstanceViews = getSubcircuitInstanceViews();
    List<Component> components = createComponents(staticViews, subcircuitInstanceViews, subcircuitSimulation);

    simulationStarted(staticViews, subcircuitSimulation);

    List<ConnectionView> connectionViews = getConnectionViews(staticViews, subcircuitInstanceViews, traceViews);

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(connectionViews, subcircuitSimulation);

    fireConnectionEvents(updatedConnectionViews, subcircuitSimulation);

    return components;
  }

  public SubcircuitTopSimulation createSubcircuitTopSimulation()
  {
    SubcircuitTopSimulation subcircuitTopSimulation = new SubcircuitTopSimulation(new CircuitSimulation());
    addSubcircuitSimulation(subcircuitTopSimulation);
    return subcircuitTopSimulation;
  }

  public List<SubcircuitSimulation> getSubcircuitSimulations(CircuitSimulation circuitSimulation)
  {
    return simulations.getSubcircuitSimulations(circuitSimulation);
  }

  public SubcircuitSimulations getSimulations()
  {
    return simulations;
  }

  public List<SubcircuitTopSimulation> getTopSimulations()
  {
    List<SubcircuitTopSimulation> list = new ArrayList<>();
    for (SubcircuitTopSimulation subcircuitSimulation : simulations.getSubcircuitTopSimulations())
    {
      list.add(subcircuitSimulation);
    }
    return list;
  }

  public void addSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation)
  {
    simulations.add(subcircuitSimulation);
  }

  public void validateSimulations(List<CircuitInstanceView> orderedTopDownCircuitInstanceViews)
  {
    if (orderedTopDownCircuitInstanceViews.size() == 0)
    {
      throw new SimulatorException("Expected at least one circuit instance view.");
    }
    CircuitInstanceView circuitInstanceView = orderedTopDownCircuitInstanceViews.get(0);
    if (circuitInstanceView.getCircuitSubcircuitView() != this)
    {
      throw new SimulatorException("First circuit instance view");
    }

    simulations.validate(orderedTopDownCircuitInstanceViews);
  }
}

