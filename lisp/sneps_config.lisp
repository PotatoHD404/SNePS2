;;; This file is used to configure SNePS. Comments are given before each entry.

(setf
 
 ;; *sneps-directory*  specifies the home directory of SNePS. If running the 
 ;; executable, this is the directory the executable is located in. If running 
 ;; the source version of SNePS, this is the directory that contains 
 ;; load-sneps.lisp. This variable must be specified.
 
 *sneps-directory* "C:\\KR Systems\\sneps\\Sneps-2.7.0"

 ;; *use-gui-show* specifies whether to use the GUI version of show or the dot 
 ;; version of show. A value of 't' specifies the GUI, while 'nil' specifies 
 ;; dot. Commenting this out defualts to 't'. Non-windows users will
 ;; have to edit the Jlinker/jl-config.cl file in the sneps-directory,
 ;; which asks the user to point jlinker to the java home directory. Java
 ;; version 1.5 or greater is needed to run the GUI.
 
 *use-gui-show* t


 ;; *jung-directory* specifies the directory containing the jung-1.7.6.jar
 ;; file, which is part of the JUNG network visualization package. Note: 
 ;; Versions of Jung greater than 1.7.6 will also work.
 ;; This variable must be specified if *use-gui-show* is 't', unless your 
 ;; system's CLASSPATH environment variable is set up to include the directory 
 ;; already.

 *jung-directory* "C:\\KR Systems\\sneps\\Sneps-2.7.0\\SnepsGUI\\SnepsGUIMods\\JungFiles\\JUNG\\jung-1.7.6"


 ;; *colt-directory* specifies the directory containing the colt.jar and
 ;;  concurrent.jar files, which are part of the Cern COLT software package. 
 ;; This variable must be specified if *use-gui-show* is 't', unless your 
 ;; system's CLASSPATH environment variable is set up to include the directory 
 ;; already.

 *colt-directory* "C:\\KR Systems\\sneps\\Sneps-2.7.0\\SnepsGUI\\SnepsGUIMods\\JungFiles\\Colt\\lib"


 ;; *xerces-directory* specifies the directory containing the resolver.jar,
 ;; xercesImpl.jar, xercesSamples.jar, and xml-apis.jar files, which are part 
 ;; of the Xerxes software package. 
 ;; This variable must be specified if *use-gui-show* is 't', unless your 
 ;; system's CLASSPATH environment variable is set up to include the directory 
 ;; already.

 *xerces-directory* "C:\\KR Systems\\sneps\\Sneps-2.7.0\\SnepsGUI\\SnepsGUIMods\\JungFiles\\Xerxes"

 ;; *commons-directory* specifies the directory containing the 
 ;; commons-collections-3.2.jar and commons-collections-testframework-3.2.jar 
 ;; files, which are part of the Jakarta Commons Collection software package. 
 ;; Note: Versions greater than 3.2 will most likely work as well.
 ;; This variable must be specified if *use-gui-show* is 't', unless your 
 ;; system's CLASSPATH environment variable is set up to include the directory 
 ;; already.

 *commons-directory* "C:\\KR Systems\\sneps\\Sneps-2.7.0\\SnepsGUI\\SnepsGUIMods\\JungFiles\\Commons"

 ;; *jimi-directory* specifies the directory containing the jimi-1.0.jar
 ;; file, which is part of the JIMI software package. 
 ;; Note: Versions greater than 1.0 will most likely work as well.
 ;; This variable must be specified if *use-gui-show* is 't', unless your 
 ;; system's CLASSPATH environment variable is set up to include the directory 
 ;; already.

 *jimi-directory* "C:\\KR Systems\\sneps\\Sneps-2.7.0\\SnepsGUI\\SnepsGUIMods\\Jimi"

 ;; *eps-dump-directory* specifies the directory containing the dump.jar
 ;; file, which is part of the EPSDump software package. 
 ;; This package is not necessary to use the SNePS Show command, and
 ;; if your system does not have the package, setting this path to 'nil'
 ;; will prevent the system from expecting the package.

 *eps-dump-directory* "C:\\KR Systems\\sneps\\Sneps-2.7.0\\SnepsGUI\\SnepsGUIMods\\JungFiles\\EPSComponentDump"

 ;; *force-jlinker-config*: On windows paltforms jlinker attempts to configure
 ;; itself by inspecting the Windows registery. This should be sufficient for
 ;; running the SNePS GUI Show (*use-gui-show* is set to 't'). If the show
 ;; command does not work, uncomment the following variable binding and modify 
 ;; the jl-config.cl file in the Jlinker subdirectory.
 ;; 
 *force-jlinker-config* t

 ;; *sneps-patch-directory* specifies the directory containing patch files. 
 ;;  Must be specified. The below setting should be fine for most users.
 ;;
 *sneps-patch-directory* *sneps-directory*

 ;; *sneps-default-lisp-extension* specifies the file extension for lisp files.
 ;; Defaults to ".lisp". This is not needed by the SNePS executeable.
 ;;
 ;; *sneps-default-lisp-extension* "lisp"

 ;; *sneps-binary-extension* specifies the compiled extension for lisp files. 
 ;; Defaults  based on current lisp implementation running. Not required by 
 ;; SNePS executeable.
 ;;
 ;; *sneps-binary-extension* "fasl"

 ;; *sneps-no-query* specifies whether to query for compilation for files or 
 ;; not.  Defaults to 't'. Not needed by the SNEPS executeable.
 ;;
 *sneps-noquery* t
 
 ;; *sneps-verbose* specifies verbose loading of SNePS files
 ;; Default to 'nil'
 ;;
 ;; *sneps-verbose* nil
 
 ;; *sneps-load-old-englex*: If this is T the old englex package is loaded 
 ;; instead of Chris Lusardi's new implementation (for compatibility with old 
 ;; stuff). Not needed by the SNePS executable
 ;;
 ;; *sneps-load-old-englex* nil
 
 ;; *sneps-user-translations*: List of user supplied logical pathname 
 ;; translations.
 ;; Changing the value of this variable after this file was loaded
 ;; will not have any effect."
 ;;
 ;; *sneps-user-translations* nil

 ;; *sneps-use-lpmk*: If non-NIL Mark Kantrowitz's logical pathnames package 
 ;; is used. This should only be non-NIL if there is no native implementation 
 ;; of logical pathnames available in your Lisp.  If the value is `:redefine'
 ;; then additionally standard Lisp functions such as `load', `open',
 ;; etc., will be redefined with LPMK versions that understand logical
 ;; pathnames.  You can explicitly trigger redefinition by calling
 ;; `(lp:redefine-standard-functions)'.  Redefinition is not necessary to
 ;; run SNePS, but it gives you the extra convenience of being able to use
 ;; logical pathnames throughout.  Some Lisps lock the Lisp package, in
 ;; which case you might have to find out how to break that lock before
 ;; you can use redefinition.  Even if there is a native implementation of
 ;; logical pathnames available it might not work for SNePS.  In this case
 ;; setting this variable to `:redefine' might solve the problem by
 ;; redefining the native implementation with LPMK. Not needed by the SNePS
 ;; executeable.
 ;;
 ;; *sneps-use-lpmk* (not (fboundp 'translate-logical-pathname))

 ;; *sneps-debug*: Set 'nil' for compiled code that is faster, but harder to 
 ;; debug. Set 't' for compiled code that is easier to debug, but slower.
 ;; Not needed by the SNePS executeable
 ;;
 ;;*sneps-debug* nil

)
(load "snepslog-helper.lisp")
(snepslog-helper:run-demo)