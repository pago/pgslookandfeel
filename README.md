About
============

The PgsLookAndFeel is a modern, cross platform LookAndFeel for Java/Swing.

It’s goal is to provide a single look and feel for applications on Windows, Gnome and KDE. It is free and OpenSource, licensed under terms of the Apache Software License 2.

Usage
============
You may choose the "everyday" version of setting a PLAF:

```java
try {
	UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
	// Is your UI already created? So you will have to update the component-tree
	// of your current frame (or actually all of them...)
	SwingUtilities.updateComponentTreeUI(yourFrame);
} catch(Exception e) { /* Most of the time you're just going to ignore it */ }
```

As an alternative both PlafOptions and PgsLookAndFeel provide static methods to do both of it:

```java
PlafOptions.setAsLookAndFeel();
PlafOptions.updateAllUIs();
```

Why is this better?

* You don't have to think of exception-handling that you're going to ignore anyway.
* You don't have to update all frames, because the updateAllUIs-method does this by itself!

Please notice: It does not matter on what of the two classes you call the two methods. PlafOptions calls the right methods on the PgsLookAndFeel (same goes for setCurrentTheme).

You may ask why I've integrated them into PlafOptions, right? Well, it's because I wanted to have every configuration/option to be inside of it, so you have only to work with one class.
