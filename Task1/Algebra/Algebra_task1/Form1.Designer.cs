namespace Algebra_task1
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.GenerateButton = new System.Windows.Forms.Button();
            this.ReadMatrixFromFile = new System.Windows.Forms.Button();
            this.FirstMatrixGrid = new System.Windows.Forms.DataGridView();
            this.matrixBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.label1 = new System.Windows.Forms.Label();
            this.ApplyChangesOne = new System.Windows.Forms.Button();
            this.WriteInFileOne = new System.Windows.Forms.Button();
            this.CuthillMcKee = new System.Windows.Forms.Button();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.twoDimMatrixBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.label2 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.FirstMatrixGrid)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.matrixBindingSource)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.twoDimMatrixBindingSource)).BeginInit();
            this.SuspendLayout();
            // 
            // GenerateButton
            // 
            this.GenerateButton.Font = new System.Drawing.Font("Consolas", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.GenerateButton.Location = new System.Drawing.Point(150, 430);
            this.GenerateButton.Name = "GenerateButton";
            this.GenerateButton.Size = new System.Drawing.Size(121, 40);
            this.GenerateButton.TabIndex = 12;
            this.GenerateButton.Text = "Generate";
            this.GenerateButton.UseVisualStyleBackColor = true;
            this.GenerateButton.Click += new System.EventHandler(this.GenerateButton_Click);
            // 
            // ReadMatrixFromFile
            // 
            this.ReadMatrixFromFile.Font = new System.Drawing.Font("Consolas", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.ReadMatrixFromFile.Location = new System.Drawing.Point(123, 372);
            this.ReadMatrixFromFile.Name = "ReadMatrixFromFile";
            this.ReadMatrixFromFile.Size = new System.Drawing.Size(184, 40);
            this.ReadMatrixFromFile.TabIndex = 13;
            this.ReadMatrixFromFile.Text = "Read from file";
            this.ReadMatrixFromFile.UseVisualStyleBackColor = true;
            this.ReadMatrixFromFile.Click += new System.EventHandler(this.ReadMatrixFromFile_Click);
            // 
            // FirstMatrixGrid
            // 
            this.FirstMatrixGrid.AllowUserToOrderColumns = true;
            this.FirstMatrixGrid.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.FirstMatrixGrid.Location = new System.Drawing.Point(12, 90);
            this.FirstMatrixGrid.Name = "FirstMatrixGrid";
            this.FirstMatrixGrid.RowHeadersWidthSizeMode = System.Windows.Forms.DataGridViewRowHeadersWidthSizeMode.AutoSizeToFirstHeader;
            this.FirstMatrixGrid.RowTemplate.Height = 24;
            this.FirstMatrixGrid.Size = new System.Drawing.Size(447, 276);
            this.FirstMatrixGrid.TabIndex = 10;
            // 
            // matrixBindingSource
            // 
            this.matrixBindingSource.DataMember = "Matrix";
            this.matrixBindingSource.DataSource = this.twoDimMatrixBindingSource;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Consolas", 19.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.label1.Location = new System.Drawing.Point(116, 49);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(233, 38);
            this.label1.TabIndex = 18;
            this.label1.Text = "First Matrix";
            // 
            // ApplyChangesOne
            // 
            this.ApplyChangesOne.Font = new System.Drawing.Font("Consolas", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.ApplyChangesOne.Location = new System.Drawing.Point(119, 488);
            this.ApplyChangesOne.Name = "ApplyChangesOne";
            this.ApplyChangesOne.Size = new System.Drawing.Size(188, 40);
            this.ApplyChangesOne.TabIndex = 23;
            this.ApplyChangesOne.Text = "Apply changes";
            this.ApplyChangesOne.UseVisualStyleBackColor = true;
            this.ApplyChangesOne.Click += new System.EventHandler(this.ApplyChangesOne_Click);
            // 
            // WriteInFileOne
            // 
            this.WriteInFileOne.Font = new System.Drawing.Font("Consolas", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.WriteInFileOne.Location = new System.Drawing.Point(123, 615);
            this.WriteInFileOne.Name = "WriteInFileOne";
            this.WriteInFileOne.Size = new System.Drawing.Size(184, 40);
            this.WriteInFileOne.TabIndex = 29;
            this.WriteInFileOne.Text = "Write into file";
            this.WriteInFileOne.UseVisualStyleBackColor = true;
            this.WriteInFileOne.Click += new System.EventHandler(this.WriteInFileOne_Click);
            // 
            // CuthillMcKee
            // 
            this.CuthillMcKee.Font = new System.Drawing.Font("Consolas", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.CuthillMcKee.Location = new System.Drawing.Point(119, 553);
            this.CuthillMcKee.Name = "CuthillMcKee";
            this.CuthillMcKee.Size = new System.Drawing.Size(184, 40);
            this.CuthillMcKee.TabIndex = 30;
            this.CuthillMcKee.Text = "Cuthill-McKee";
            this.CuthillMcKee.UseVisualStyleBackColor = true;
            this.CuthillMcKee.Click += new System.EventHandler(this.CuthillMcKee_Click);
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(755, 90);
            this.textBox1.Multiline = true;
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(314, 367);
            this.textBox1.TabIndex = 31;
            // 
            // twoDimMatrixBindingSource
            // 
            this.twoDimMatrixBindingSource.DataSource = typeof(Algebra_task1.TwoDimMatrix);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Consolas", 19.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.label2.Location = new System.Drawing.Point(850, 49);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(125, 38);
            this.label2.TabIndex = 32;
            this.label2.Text = "Result";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1328, 690);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.CuthillMcKee);
            this.Controls.Add(this.WriteInFileOne);
            this.Controls.Add(this.ApplyChangesOne);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.FirstMatrixGrid);
            this.Controls.Add(this.ReadMatrixFromFile);
            this.Controls.Add(this.GenerateButton);
            this.Name = "Form1";
            this.Text = "Form1";
            ((System.ComponentModel.ISupportInitialize)(this.FirstMatrixGrid)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.matrixBindingSource)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.twoDimMatrixBindingSource)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion
        private System.Windows.Forms.Button GenerateButton;
        private System.Windows.Forms.Button ReadMatrixFromFile;
        private System.Windows.Forms.DataGridView FirstMatrixGrid;
        private System.Windows.Forms.DataGridViewTextBoxColumn nDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn mDataGridViewTextBoxColumn;
        private System.Windows.Forms.BindingSource twoDimMatrixBindingSource;
        private System.Windows.Forms.BindingSource matrixBindingSource;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button ApplyChangesOne;
        private System.Windows.Forms.Button WriteInFileOne;
        private System.Windows.Forms.Button CuthillMcKee;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Label label2;
    }
}

