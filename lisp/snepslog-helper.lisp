(defpackage :snepslog-helper
  (:use :common-lisp :excl)
  (:export :run-demo))

(in-package :snepslog-helper)

(defun run-snepslog-demo ()
    (snepslog:tell "(\~p <=> q) <=> (p <=> ~q)?")


    (sneps:show))

(defun run-demo ()
  (run-snepslog-demo))