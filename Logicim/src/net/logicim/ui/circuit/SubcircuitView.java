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
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.passive.common.Passive;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
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
import net.logicim.ui.connection.LocalConnectionNet;
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

  public SubcircuitView(CircuitSimulation circuitSimulation)
  {
    this();
    this.simulations.add(createSubcircuitSimulation(circuitSimulation));
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
    views.addAll(subcircuitInstanceViews);
    views.addAll(integratedCircuitViews);
    return views;
  }

  public void disconnectTraceView(TraceView traceView)
  {
    List<ConnectionView> connectionViews = traceView.getConnectionViews();
    connectionViewCache.removeAll(traceView, connectionViews);
    traceView.disconnect();
  }

  public List<ConnectionView> disconnectStaticView(StaticView<?> staticView, SubcircuitSimulation subcircuitSimulation)
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
    staticView.disconnect();

    if (staticView instanceof ComponentView)
    {
      Component component = ((ComponentView<?>) staticView).getComponent(subcircuitSimulation);
      if (component != null)
      {
        Simulation simulation = subcircuitSimulation.getSimulation();
        subcircuitSimulation.getCircuit().disconnectComponent(component, simulation);
      }
    }

    return connectionViews;
  }

  public void deleteComponentView(StaticView<?> staticView,
                                  SubcircuitSimulation subcircuitSimulation)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);
    deleteComponentViews(staticViews, subcircuitSimulation);
  }

  public void deleteComponentViews(List<StaticView<?>> staticViews,
                                   SubcircuitSimulation subcircuitSimulation)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> componentView : staticViews)
    {
      if (componentView == null)
      {
        throw new SimulatorException("Cannot delete a [null] view.");
      }

      List<ConnectionView> disconnectedConnectionViews = disconnectStaticView(componentView, subcircuitSimulation);
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
        deleteIntegratedCircuit((IntegratedCircuitView<?, ?>) componentView, subcircuitSimulation);
      }
      else if (componentView instanceof PassiveView)
      {
        deletePassiveView((PassiveView<?, ?>) componentView, subcircuitSimulation);
      }
      else if (componentView instanceof SubcircuitInstanceView)
      {
        deleteSubcircuitInstanceView((SubcircuitInstanceView) componentView, subcircuitSimulation);
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

  protected void deleteIntegratedCircuit(IntegratedCircuitView<?, ?> integratedCircuitView,
                                         SubcircuitSimulation subcircuitSimulation)
  {
    if (subcircuitSimulation != null)
    {
      IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getComponent(subcircuitSimulation);
      subcircuitSimulation.getCircuit().remove(integratedCircuit);
    }
    removeIntegratedCircuitView(integratedCircuitView);
  }

  protected void deletePassiveView(PassiveView<?, ?> passiveView,
                                   SubcircuitSimulation subcircuitSimulation)
  {
    if (subcircuitSimulation != null)
    {
      Passive passive = passiveView.getComponent(subcircuitSimulation);
      subcircuitSimulation.getCircuit().remove(passive);
    }
    removePassiveView(passiveView);
  }

  protected void deleteSubcircuitInstanceView(SubcircuitInstanceView subcircuitInstanceView,
                                              SubcircuitSimulation subcircuitSimulation)
  {
    if (subcircuitSimulation != null)
    {
      SubcircuitInstance subcircuitInstance = subcircuitInstanceView.getComponent(subcircuitSimulation);
      subcircuitSimulation.getCircuit().remove(subcircuitInstance);
    }
    removeSubcircuitInstanceView(subcircuitInstanceView);
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

  public void validateConsistency()
  {
    validateConnectionViews();
    validateTracesContainOnlyCurrentViews();
    validateTunnelViews();
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
                                                            SubcircuitSimulation subcircuitSimulation)
  {
    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    if (subcircuitSimulation != null)
    {
      for (ConnectionView connectionView : connectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("Connection may not be null.");
        }

        if (!updatedConnectionViews.contains(connectionView))
        {
          List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(subcircuitSimulation, connectionView);
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

  public List<ConnectionView> getConnectionViews(List<StaticView<?>> staticViews, Set<TraceView> traceViews)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.getConnectionViews());
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

  private List<Component> createComponents(List<StaticView<?>> staticViews, SubcircuitSimulation subcircuitSimulation)
  {
    List<Component> components = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      Component component = staticView.createComponent(subcircuitSimulation);
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
          PortView portView = componentView.getPort(connectionView);
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

  public void deleteTraceViews(Set<TraceView> inputTraceViews,
                               SubcircuitSimulation subcircuitSimulation)
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

  protected void findAndConnectNonTraceViewsForDeletion(SubcircuitSimulation subcircuitSimulation,
                                                        Set<ConnectionView> nonTraceConnectionViews)
  {
    if (subcircuitSimulation != null)
    {
      Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
      for (ConnectionView nonTraceConnectionView : nonTraceConnectionViews)
      {
        List<LocalConnectionNet> connectionNets = PortTraceFinder.findAndConnectTraces(subcircuitSimulation, nonTraceConnectionView);
        updatedConnectionViews.addAll(PortTraceFinder.getConnectionViews(connectionNets));
      }
      fireConnectionEvents(updatedConnectionViews, subcircuitSimulation);
    }
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
    ArrayList<StaticData<?>> componentDatas = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      StaticData<?> staticData = (StaticData<?>) staticView.save(selection.contains(staticView));
      if (staticData == null)
      {
        throw new SimulatorException("%s save may not return null.", staticView.toIdentifierString());
      }

      componentDatas.add(staticData);
    }

    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceData traceData = traceView.save(selection.contains(traceView));
      traceDatas.add(traceData);
    }

    SubcircuitData subcircuitData = new SubcircuitData(componentDatas, traceDatas);
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
    for (StaticView<?> staticView : staticViews)
    {
      staticView.disable();
      connectionViews.addAll(disconnectStaticView(staticView, subcircuitSimulation));
      staticView.destroyComponent(subcircuitSimulation);
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
                                       List<StaticView<?>> staticViews,
                                       List<TraceView> traceViews,
                                       Set<StaticView<?>> selectedViews)
  {
    List<Line> newLines = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      newLines.add(traceView.getLine());
    }
    removeTraceViews(new LinkedHashSet<>(traceViews));

    List<ConnectionView> createdConnectionViews = getOrCreateStaticViewConnections(staticViews);
    Set<ConnectionView> junctions = getComponentConnectionPositions(staticViews);
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

    enableStaticViews(staticViews);

    createComponents(staticViews, subcircuitSimulation);

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(createdConnectionViews, subcircuitSimulation);
    simulationStarted(staticViews, subcircuitSimulation);

    Set<TraceView> existingTraces = createTraceViews(existingLines, subcircuitSimulation);
    Set<ConnectionView> updatedCreatedTraceConnectionViews = connectCreatedTraceViews(existingTraces, subcircuitSimulation);
    updatedConnectionViews.addAll(updatedCreatedTraceConnectionViews);

    Set<TraceView> newTraces = createTraceViews(newLines, subcircuitSimulation);
    updatedConnectionViews.addAll(connectCreatedTraceViews(newTraces, subcircuitSimulation));

    fireConnectionEvents(updatedConnectionViews, subcircuitSimulation);

    return calculateNewSelection(staticViews, selectedViews, newTraces);
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

  public void destroyComponents(CircuitSimulation circuitSimulation)
  {
    List<StaticView<?>> staticViews = getStaticViews();
    for (StaticView<?> staticView : staticViews)
    {
      staticView.destroyComponent(circuitSimulation);
    }

    for (TraceView traceView : traceViews)
    {
      traceView.destroyComponent(circuitSimulation);
    }

    simulations.remove(circuitSimulation);
  }

  public List<Component> createComponents(SubcircuitSimulation subcircuitSimulation)
  {
    List<StaticView<?>> staticViews = getStaticViews();
    List<Component> components = createComponents(staticViews, subcircuitSimulation);

    simulationStarted(staticViews, subcircuitSimulation);

    List<ConnectionView> connectionViews = getConnectionViews(staticViews, traceViews);

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(connectionViews, subcircuitSimulation);

    fireConnectionEvents(updatedConnectionViews, subcircuitSimulation);

    return components;
  }

  public SubcircuitTopSimulation createSubcircuitSimulation(CircuitSimulation circuitSimulation)
  {
    return new SubcircuitTopSimulation(circuitSimulation);
  }

  public SubcircuitSimulation getSubcircuitSimulation(CircuitSimulation circuitSimulation)
  {
    return simulations.get(circuitSimulation);
  }

  public SubcircuitSimulations getSimulations()
  {
    return simulations;
  }

  public List<SubcircuitTopSimulation> getTopSimulations()
  {
    List<SubcircuitTopSimulation> list = new ArrayList<>();
    for (SubcircuitSimulation subcircuitSimulation : simulations.getSubcircuitSimulations())
    {
      if (subcircuitSimulation instanceof SubcircuitTopSimulation)
      {
        list.add((SubcircuitTopSimulation) subcircuitSimulation);
      }
    }
    return list;
  }

  public void addSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation)
  {
    simulations.add(subcircuitSimulation);
  }

  public void validateSimulations(List<CircuitInstanceView> circuitInstanceViews)
  {
    if (circuitInstanceViews.size() == 0)
    {
      throw new SimulatorException("Expected at least one circuit instance view.");
    }
    CircuitInstanceView circuitInstanceView = circuitInstanceViews.get(0);
    if (circuitInstanceView.getCircuitSubcircuitView() != this)
    {
      throw new SimulatorException("First circuit instance view");
    }

    Collection<SubcircuitSimulation> expectedSubcircuitSimulations = simulations.getSubcircuitSimulations();

    for (CircuitInstanceView instanceView : circuitInstanceViews)
    {
      Collection<SubcircuitSimulation> simulations = instanceView.getSubcircuitSimulations();
      List<SubcircuitSimulation> missingSimulations = new ArrayList<>();
      for (SubcircuitSimulation expectedSubcircuitSimulation : expectedSubcircuitSimulations)
      {
        if (!simulations.contains(expectedSubcircuitSimulation))
        {
          missingSimulations.add(expectedSubcircuitSimulation);
        }
      }
      if (missingSimulations.size() > 0)
      {
        throw new SimulatorException("Expected simulations [%s] were not found in circuit instance [%s].", missingSimulations.size(), instanceView.getDescription());
      }
    }
  }
}

